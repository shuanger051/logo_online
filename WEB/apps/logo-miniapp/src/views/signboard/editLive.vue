<template>
  <div style="height: 100%">
    <div class="edit-live-header">
      <van-button type="primary" class="recover" @click="confirmDialog = true"
        >确认完成</van-button
      >
      <van-uploader
        class="upload"
        :after-read="afterRead"
        :max-size="1024 * 1024 * 10"
        @oversize="onOversize"
      >
        <!-- <span>上传实景图</span> -->
        <van-button plain type="warning">上传实景图</van-button>
      </van-uploader>

      <!-- <span @click="download">下载</span> -->
    </div>
    <div class="edit-live">
      <div id="edit-live__wrap">
        <shape
          :handle-element-move-prop="handleElementMove"
          :handle-mousedown-prop="noop"
          :handle-point-move-prop="handleElementMove"
          :handle-element-mouse-up-prop="noop"
          :handle-rotation-prop="handleRotationProp"
          :handle-point-mouse-up-prop="noop"
          :default-position="style"
          :active="active"
          :delIcon="false"
          :style="getStyle()"
          class="shape_wrap"
          v-if="signboardPic"
        >
          <div class="shape_content">
            <img :src="signboardPic" width="100%" />
          </div>
        </shape>
        <van-image width="100%" v-if="livePic" :src="livePic" />
      </div>
    </div>
    <van-dialog
      v-model="confirmDialog"
      title="确认完成"
      @confirm="downloadInfo()"
    >
      <div style="padding: 10px">
        <p style="font-size: 0.4rem">
          已为您准备好店招素材和效果图，请下载到本地，可在后续流程中使用。本效果图
          及素材仅供参考
        </p>
      </div>
    </van-dialog>
  </div>
</template>
<script>
import store from "core/mobile/store/index";
import {
  appUploadMaterialAttachmentBase64APIOSS,
  appUploadMaterialAttachmentOSS,
} from "core/api/";
import { resolveImgUrlBase64 } from "core/support/imgUrl";
import shape from "core/support/shape_mobile";
import { download, downLoadXLSL } from "core/support/download.js";
import { sleep } from "@editor/utils/tool";
import { mapActions, mapState } from "vuex";
import { Toast } from "vant";
import { downloadPoster } from "@editor/utils/canvas-helper.js";
import { Notify } from "vant";
export default {
  store,
  components: {
    shape,
  },
  watch: {
    "$store.state.editor.signboardPic": {
      async handler(n) {
        if (n) {
          this.signboardPic = await resolveImgUrlBase64(n);
        }
      },
      immediate: true,
    },
    "$store.state.editor.livePic": {
      async handler(n) {
        if (n) {
          this.livePic = await resolveImgUrlBase64(n);
        }
      },
      immediate: true,
    },
  },
  data() {
    return {
      style: {
        left: 20,
        top: 20,
        width: 300,
        height: 300,
        angle: 0,
      },
      confirmDialog: false,
      active: true,
      livePic: null,
      signboardPic: null,
    };
  },
  created() {},
  methods: {
    ...mapActions("editor", ["setPic", "mCreateCover"]),
    handleElementMove(pos) {
      this.style = {
        ...this.style,
        ...pos,
      };
    },
    async afterRead(file) {
      const toast = Toast.loading({
        message: "上传中",
        forbidClick: true,
        duration: 0,
      });
      const form = new FormData();
      form.append("file", file.file);
      const info = await appUploadMaterialAttachmentOSS(form);
      this.setPic({
        type: "livePic",
        value: info.data.urlPath,
      });
      toast.clear();
    },
    async download() {
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        await downloadPoster({ selector: "#edit-live__wrap" });
      } catch (e) {
        Notify({ type: "danger", message: "创建失败" });
      }
      toast.clear();
    },
    async creatLivePic() {
      const toast = Toast.loading({
        message: "生成实景合成图...",
        forbidClick: true,
        duration: 0,
      });
      try {
        const info = await this.mCreateCover({ el: "#edit-live__wrap" });
        this.setPic({
          type: "composePic",
          value: info.data.urlPath,
        });
        Notify({ type: "success", message: "创建成功" });
        // 创建成功直接下载
        await download(info.data.urlPath, "实景效果图");
      } catch (e) {
        console.log(e);
        Notify({ type: "danger", message: "创建失败" });
      }
      await sleep(1000);
      toast.clear();
    },
    async downloadInfo() {
      // 未上传实景图弹窗提示
      if (!this.livePic) {
        Notify({ message: "请先上传实景图！", type: "warning" });
        return;
      } else {
        await this.creatLivePic();
        await this.picDownload();
        await this.xlslDownload();
      }

    },
    async xlslDownload() {
      const toast = Toast.loading({
        message: "下载店招素材中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        await downLoadXLSL(this.$store.state.editor.work)
      } catch (e) {
        Notify({ type: "danger", message: "下载失败" });
      }
      toast.clear();
    },
    async picDownload() {
      const toast = Toast.loading({
        message: "下载店招图片...",
        forbidClick: true,
        duration: 0,
      });
      try {
        await download(this.$store.state.editor.signboardPic, "店招图片");
      } catch (e) {
        Notify({ type: "danger", message: "下载失败" });
      }
      await sleep(1000);
      toast.clear();
    },
    handleRotationProp(angle) {
      this.style.angle = angle;
    },
    getStyle() {
      const style = Object.assign({}, this.style);
      Object.entries(style).forEach(([key, val]) => {
        if (typeof val == "string") {
          style[key] = val;
        } else {
          style[key] = val + "px";
        }
      });
      style.transform = `rotate(${this.style.angle}deg)`;
      return style;
    },
    onOversize(file) {
      Toast("文件大小不能超过 10M");
    },
    noop() {},
  },
  created() {
    if (!this.$store.state.editor.livePic) {
      Notify({ type: "warning", message: "请上传实景图" });
    }
  },
};
</script>
<style lang="scss" scoped>
.edit-live {
  background-color: #9d9c9c;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: calc(100% - 50px);
}
#edit-live__wrap {
  position: relative;
  min-height: 200px;
}
.edit-live__content {
  position: absolute;
  z-index: 100;
  padding: 10px;
  &.active {
    border: 1px solid #fa7a36;
  }
  .icon-fa-scale {
    position: absolute;
    bottom: -25px;
    right: -22px;
  }
}
.shape_wrap {
  z-index: 100;
  position: absolute;
}
.recover {
  position: absolute;
  right: 15px;
  .van-button__text {
    color: #fff;
  }
}
.shape_content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
}
.edit-live-header {
  height: 50px;
  overflow: hidden;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  .upload {
    position: absolute;
    left: 10px;
    color: #fa7a36;
  }
  > span {
    color: #fa7a36;
    position: absolute;
    right: 10px;
  }
}
</style>

<template>
  <div style="height: 100%">
    <div class="edit-live-header">
      <van-button type="primary" class="recover" @click="creatLivePic"
        >提交备案</van-button
      >
      <van-uploader
        class='upload'
        :after-read="afterRead"
        :max-size="1024 * 1024 * 2"
        @oversize="onOversize"
      >
      <!-- <span>上传实景图</span> -->
      <van-button plain type="warning">上传实景图</van-button>

      </van-uploader>

      <span @click="download">下载</span>
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
  </div>
</template>
<script>
import store from "core/mobile/store/index";
import { appUploadMaterialAttachment } from "core/api/";
import { resolveImgUrlBase64 } from "core/support/imgUrl";
import shape from "core/support/shape_mobile";
import { mapActions } from "vuex";
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
      active: true,
      livePic: null,
      signboardPic: null,
    };
  },
  created() {
  },
  methods: {
    ...mapActions("editor", [
      "setPic",
      "mCreateCover",
    ]),
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
      const info = await appUploadMaterialAttachment(form);
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
        message: "生成中...",
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
        this.$router.push({
          path: "/signboard/editConfirm"
        });
      } catch (e) {
        console.log(e)
        Notify({ type: "danger", message: "创建失败" });
      }
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
      Toast("文件大小不能超过 2M");
    },
    noop() {},
  },
  created() {
    if (!this.$store.state.editor.livePic) {
      Notify({ type: 'danger', message: '请先上传实景图片',  duration: 3000});
    }
  }
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

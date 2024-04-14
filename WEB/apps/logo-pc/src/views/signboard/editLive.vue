<template>
  <div class="edit-live">
    <div class="edit-live__wrap">
      <div class="edit-live-bar flex">
        <div class="edit-live-bar__left flex">
          <div class="flex">
            <a-upload
              name="file"
              :customRequest="upload"
              :showUploadList="false"
            >
              <a-icon type="picture" />
              <span style="color: #fa7a36">上传实景图</span>
            </a-upload>
          </div>
        </div>
        <div class="edit-live-bar__right flex">
          <div class="flex" @click="confirmDialog=true">
            <a-icon type="idcard" /> <span>确认完成</span>
          </div>
        </div>
      </div>

      <div
        class="edit-live__main"
        :class="{
          'active-screen-shot': tokenScreenShotStatus,
        }"
      >
        <div id="edit-live__container">
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
          <div class="live_pic" v-if="livePic">
            <img :src="livePic" width="100%" />
          </div>
        </div>
      </div>
      <a-modal v-model="confirmDialog" @ok="handlerOk" title="确认" cancelText="取消" okText="确定">
        <p style="font-size: 14">
          已为您准备好店招素材和效果图，请下载到本地，可在后续流程中使用。本效果图
          及素材仅供参考
        </p>
      </a-modal>
    </div>
  </div>
</template>
<script>
import store from "core/pc/store";
import { appUploadMaterialAttachmentOSS } from "core/api/";
import { mapActions, mapState } from "vuex";
import shape from "core/support/shape";
import { resolveImgUrlBase64 } from "core/support/imgUrl";
import { sleep } from "@editor/utils/tool";
import { download, downLoadXLSL } from "core/support/download.js";

export default {
  store,
  data() {
    return {
      style: {
        left: 250,
        top: 150,
        width: 300,
        height: 300,
        angle: 0,
      },
      active: true,
      signboardPic: null,
      livePic: null,
      confirmDialog: false
    };
  },
  computed: {
    ...mapState("editor", ["tokenScreenShotStatus"]),
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
  components: {
    shape,
  },
  methods: {
    ...mapActions("editor", [
      "setPic",
      "mCreateCover",
      "changeTokenScreenShotStatus",
    ]),
    async upload(evt) {
      const form = new FormData();
      form.append("file", evt.file);
      const info = await appUploadMaterialAttachmentOSS(form);
      this.setPic({
        type: "livePic",
        value: info.data.urlPath,
      });
    },
    handleElementMove(pos) {
      this.style = {
        ...this.style,
        ...pos,
      };
    },
    handlerOk() {
      this.confirmDialog=false;
      this.downloadInfo()
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
    async downloadInfo() {
      // 未上传实景图弹窗提示
      if (!this.$store.state.editor.livePic) {
        this.$notification.info({
          message: "请先上传实景图!",
        });
      }
      await this.picDownload();
      await this.creatLivePic();
      // await this.xlslDownload();
    },
    async xlslDownload() {
      const toast = this.$message.loading("下载店招素材中...", 0);
      try {
        downLoadXLSL(this.$store.state.editor.work);
      } catch (e) {
        Notify({ type: "danger", message: "下载失败" });
      }
      toast();
    },
    async picDownload() {
      const toast = this.$message.loading("下载店招图片...", 0);
      try {
        download(this.$store.state.editor.signboardPic,  "店招图片.png");
        this.$message.success("下载成功", 2);
      } catch (e) {
        console.log(e,34);
        this.$message.error("下载失败");
      }
      toast();
    },
    async creatLivePic() {
      const toast = this.$message.loading("生成中...", 0);
      this.changeTokenScreenShotStatus(true);
      await sleep(1000);
      try {
        const info = await this.mCreateCover({ el: "#edit-live__container" });
        this.setPic({
          type: "composePic",
          value: info.data.urlPath,
        });
        download(info.data.urlPath, "实景效果图.png");
      } catch (e) {
        console.log(e,33);
        this.$message.error("创建失败");
      }
      this.changeTokenScreenShotStatus(false);
      toast();
    },
    noop() {},
  },
  created() {
    if (!this.$store.state.editor.livePic) {
      this.$notification.info({
        message: "请先上传实景图!",
      });
    }
  },
};
</script>
<style lang="scss" scoped>
.edit-live {
  padding: 20px 0px;
  height: calc(100vh);
  background: #eee;
}
.edit-live-bar {
  justify-content: space-between;
  height: 45px;
  margin-bottom: 35px;
  background: #fff;
  border: 1px solid rgb(230, 229, 229);
  span {
    font-weight: bold;
    margin-left: 5px;
  }
  .ant-upload {
    margin-left: 0px;
  }
}
.edit-live-bar__left,
.edit-live-bar__right {
  div {
    margin-left: 20px;
    cursor: pointer;
  }
}
.edit-live-bar__right {
  padding-right: 20px;
}
.edit-live__wrap {
  width: 800px;
  margin: 0 auto;
}
.flex {
  display: flex;
  align-items: center;
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
#edit-live__container {
  position: relative;
  width: 100%;
}
</style>

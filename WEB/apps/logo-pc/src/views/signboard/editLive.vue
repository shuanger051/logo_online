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
          <div class="flex">
            <a-icon type="font-size" />
            <span>下载</span>
          </div>
        </div>
        <div class="edit-live-bar__right flex">
          <div class="flex" @click="creatLivePic">
            <a-icon type="idcard" /> <span>提交备案</span>
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
    </div>
  </div>
</template>
<script>
import store from "core/pc/store";
import { appUploadMaterialAttachment } from "core/api/";
import { mapActions, mapState } from "vuex";
import shape from "core/support/shape";
import { resolveImgUrlBase64 } from "core/support/imgUrl";
import {sleep} from '@editor/utils/tool'

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
    };
  },
  computed: {
    ...mapState('editor', ['tokenScreenShotStatus'])
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
      const info = await appUploadMaterialAttachment(form);
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
    async creatLivePic() {
      const toast = this.$message.loading("生成中...", 0);
      this.changeTokenScreenShotStatus(true)
      await sleep(1000)
      try {
        const info = await this.mCreateCover({ el: "#edit-live__container" });
        this.setPic({
          type: "composePic",
          value: info.data.urlPath,
        });
        this.$message.success("创建成功", 1);
        // later(() => {
        //   this.$router.push({
        //     path: "/signboard/editConfirm",
        //   });
        // }, 2000);
        console.log(info.data.urlPath, 888);
      } catch (e) {
        console.log(e);
        this.$message.error("创建失败");
      }
      this.changeTokenScreenShotStatus(false)
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

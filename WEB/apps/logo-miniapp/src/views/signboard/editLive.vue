<template>
  <div style="height: 100%">
    <div class="edit-live-header">
      <van-button type="primary" class="recover" @click="creatLivePic"
        >提交承诺备案</van-button
      >
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
          v-if="currentShopSign"
        >
          <div class="shape_content">
            <img :src="currentShopSign" width="100%" />

          </div>
        </shape>
        <van-image width="100%" v-if="currentLivePic" :src="currentLivePic" />
      </div>
    </div>
  </div>
</template>
<script>
import store from "core/store/mobileIndex";
import {
  appGetLogoInfoByShopsIdOSS,
  appUploadContentAttachmentBase64OSS,
  appGetShopsInfoByIdAPI,
  appGetShopsInfoByIdAPIOSS
} from "core/api";
import { resolveImgUrl } from "core/support/imgUrl";
import { convertImageToBase64} from "@editor/utils/canvas-helper.js";

import shape from "core/support/shape";
import { Toast } from "vant";
import { takeScreenshot, downloadPoster } from "@editor/utils/canvas-helper.js";
import { Notify } from "vant";
export default {
  store,
  components: {
    shape,
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
      currentLivePic: null,
      currentShopSign: null,
    };
  },
  created() {
    appGetLogoInfoByShopsIdOSS({
      shopsId: this.$route.query.shopId,
    }).then((data) => {
      convertImageToBase64(resolveImgUrl(data.data.urlPath, true), (src) => {
        this.currentShopSign = src
      })
    });
    appGetShopsInfoByIdAPIOSS({
      shopsId: this.$route.query.shopId,
    }).then(({ data }) => {
      const list = data.list.find((item) => item.attachmentType == "1");
      if (list) {
        convertImageToBase64(resolveImgUrl(list.urlPath, true), (src) => {
        this.currentLivePic = src
      })
      }
    });
  },
  methods: {
    handleElementMove(pos) {
      this.style = {
        ...this.style,
        ...pos,
      };
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
        const file = await takeScreenshot({
          selector: "#edit-live__wrap",
          type: "dataUrl",
        });
        const { shopId } = this.$route.query;
        await appUploadContentAttachmentBase64OSS({
          base64: file,
          shopsId: shopId,
          attachmentType:4
        });
        Notify({ type: "success", message: "创建成功" });
        this.$router.push({
          path: "/signboard/editConfirm",
          query: { shopId },
        });
      } catch (e) {
        console.log(e, 888);
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
    noop() {},
  },
};
</script>
<style lang="scss" scoped>
.edit-live {
  background-color: #9d9c9c;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: calc(100% - 45px);
}
#edit-live__wrap {
  position: relative;
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
  height: 45px;
  overflow: hidden;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  > span {
    color: #fa7a36;
    position: absolute;
    right: 10px;
  }
}
</style>

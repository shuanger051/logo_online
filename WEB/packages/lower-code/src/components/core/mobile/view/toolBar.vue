<template>
  <div class="toolbar">
    <div class="edit-wrap-header">
      <span @click="openTextDialog">
        <van-icon name="home-o" />
        <font>加字</font>
      </span>
      <span @click.capture="beforUploader">
        <van-icon name="search" />
        <font>加图</font>
      </span>
      <span @click="previewImage">
        <van-icon name="friends-o" />
        <font>模板</font>
      </span>
      <van-button
        type="primary"
        class="recover"
        @click="createShopSign"
        size="small"
        >设计完成</van-button
      >
    </div>
    <van-uploader
      id="fileUploader"
      style="display: none"
      :after-read="afterRead"
      :showUploadList="false"
      :max-size="1024 * 1024 * 10"
      @oversize="onOversize"
    >
    </van-uploader>
    <van-dialog
      v-model="textDialog"
      show-cancel-button
      class="add-text-dialog"
      @confirm="addText"
    >
      <textarea v-model="text" class="add-text" placeholder="请添加文字" />
      <p style="font-size: 0.3rem;color: #646566;
">请注意，您的店招的店名需要和营业执照的名称一致或者是营业执照的名称的缩写</p>

    </van-dialog>

    <van-dialog v-model="showColor" show-confirm-button>
      <compact :value="fontColor" @input="resolveColor" :palette = "palette"></compact>
    </van-dialog>
  </div>
</template>
<script>
import store from "core/mobile/store/index";
import { mapActions, mapState } from "vuex";
import { Toast, Notify } from "vant";
import { sleep, later } from "@editor/utils/tool";
import { appUploadMaterialAttachmentOSS } from "core/api/";
import { ImagePreview } from "vant";
import { resolveImgUrlBase64 } from "core/support/imgUrl";
import { Compact } from "vue-color";

export default {
  data() {
    return {
      fontColor: { r: 0, g: 0, b: 0, a:1 },
      textDialog: false,
      picDialog: false,
      tempDialog: false,
      text: "",
      palette: window.pageContentJson.style.fontColor,
      showColor: false
    };
  },
  components: {
    compact: Compact
  },
  props: ["showPicConfirm"],
  store,
  computed: {
    ...mapState("editor", {
      elements: (state) => state.editingPage.elements,
      work: (state) => state.work,
    }),
  },
  created() {
    let style = this.$route.query.styles
    let lmcolor = window.pageContentJson.style.lmcolor
    if (style) {
      lmcolor.some((v) => {
        if (style == v.code) {
          this.palette = v.rgb
          return true
        }
      })
    }

    this.__eventBus.$on('showColor', () => {
      this.showColor = true
    })
  },
  destroyed() {
    this.__eventBus.$off('showColor')
  },
  methods: {
    ...mapActions("editor", [
      "elementManager",
      "setEditingElement",
      "setPic",
      "mCreateCover",
    ]),
    resolveColor(v) {
      this.__eventBus.$emit("resolveColor", v)
    },
    beforUploader() {
      this.showPicConfirm().then(() => {
        document.getElementById("fileUploader").click();
      });
    },
    addText() {
      if (this.text) {
        this.addElement({
          name: "lbp-text-tinymce",
          shortcutProps: {
            text: this.text,
          },
          dragStyle: {
            top: 10,
            left: 10,
          },
        });
      }
    },
    addElement(config) {
      this.elementManager({
        type: "add",
        value: {
          ...config,
        },
      });
      let currentElement = this.elements[this.elements.length - 1];
      this.setEditingElement(currentElement);
    },
    onOversize(file) {
      Toast("文件大小不能超过 10M");
    },
    async afterRead(file) {
      const toast = Toast.loading({
        message: "上传中",
        forbidClick: true,
        duration: 0,
      });
      try {
        const form = new FormData();
        form.append("file", file.file);
        const info = await appUploadMaterialAttachmentOSS(form);
        this.addElement({
          name: "lbp-picture",
          shortcutProps: {
            imgSrc: info.data.urlPath,
          },
          dragStyle: {
            top: 10,
            left: 10,
          },
        });
      } catch {
        Toast.fail("上传失败");
      }
      toast.clear();
    },
    async createShopSign() {
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        const info = await this.mCreateCover({ el: "#content_edit" });
        this.setPic({
          type: "signboardPic",
          value: info.data.urlPath,
        });
        Notify({ type: "success", message: "创建成功" });
        later(() => {
          this.$router.push({
            name: "editLive",
            query: {
              shopId: this.$route.query.shopId,
            },
          });
        }, 1000);
      } catch (e) {
        console.log(e);
        Notify({ type: "danger", message: "创建失败" });
      }
      toast.clear();
    },
    openTextDialog() {
      this.textDialog = true;
      this.text = "";
    },
    previewImage() {
      resolveImgUrlBase64(this.work.cover_image_url, false, (url) => {
        ImagePreview([url]);
      });
    },
  },
};
</script>
<style lang="less">

.toolbar .vc-compact {
  padding:10px 10px;
  width: 100%;
  box-shadow: none;
  .vc-compact-color-item {
    width: 25px;
    height: 25px;
  }
}
.toolbar {
  .van-dialog__confirm {
    color: #2f63f1;
  }
  .van-cell__value {
    overflow:visible;
  }

  .colorTitle {
    color:#646566
  }
}

.toolbar .van-dialog__content {
  padding: 15px 20px;
}
.toolbar textarea {
  width: 100%;
  height: 100px;
  border: none;
  outline: none;
  background: #eaeaea;
}
.recover {
  position: absolute;
  right: 15px;
  .van-button__text {
    color: #fff;
  }
}
</style>

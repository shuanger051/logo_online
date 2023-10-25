<template>
  <div class="toolbar">
    <van-tabbar active-color="#7d7e80" inactive-color="#7d7e80">
      <van-tabbar-item icon="home-o" @click="openTextDialog"
        >加字</van-tabbar-item
      >

      <van-tabbar-item icon="search">
        <van-uploader
          :after-read="afterRead"
          :max-size="1024 * 1024 * 2"
          @oversize="onOversize"
        >
          加图
        </van-uploader>
      </van-tabbar-item>
      <van-tabbar-item icon="friends-o" @click="previewImage">模板</van-tabbar-item>
    </van-tabbar>
    <van-dialog
      v-model="textDialog"
      show-cancel-button
      class="add-text-dialog"
      @confirm="addText"
    >
      <textarea v-model="text" class="add-text" placeholder="请添加文字" />
    </van-dialog>
  </div>
</template>
<script>
import store from "core/store/mobileIndex";
import { mapActions, mapState } from "vuex";
import { Toast } from "vant";
import { appUploadMaterialAttachment } from "core/api/";
import { ImagePreview } from 'vant';
import {resolveImgUrl} from 'core/support/imgUrl'

export default {
  data() {
    return {
      textDialog: false,
      picDialog: false,
      tempDialog: false,
      text: "",
    };
  },
  store,
  computed: {
    ...mapState("editor", {
      elements: (state) => state.editingPage.elements,
      work: (state) => state.work
    }),
  },
  methods: {
    ...mapActions("editor", ["elementManager", "setEditingElement"]),

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
      Toast("文件大小不能超过 2M");
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
        const info = await appUploadMaterialAttachment(form);
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
    openTextDialog() {
      this.textDialog = true;
      this.text = "";
    },
    previewImage() {
      ImagePreview([resolveImgUrl(this.work.cover_image_url)])
    }
  },
};
</script>
<style lang="less">
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
</style>

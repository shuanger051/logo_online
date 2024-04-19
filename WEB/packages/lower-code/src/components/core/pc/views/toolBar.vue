<template>
  <div class="tool-bar flex">
    <div class="tool-bar__left flex">
      <div
        class="flex"
        @click="
          textDialog = true;
          text = '';
        "
      >
        <a-icon type="font-size" />
        <span>加字</span>
      </div>
      <div class="flex" @click="beforUploader">
        <a-icon type="picture" />
        <span>加图</span>
      </div>
      <a-upload
        class="fileUploader"
        name="file"
        :customRequest="upload"
        style="display: none"
        howUploadList="false"
      >
      </a-upload>
      <div class="flex" @click="tempDialog = true">
        <a-icon type="idcard" /> <span>模板</span>
      </div>
    </div>
    <div class="tool-bar__right flex">

      <div class="flex" @click="createShopSign">
     <span>设计完成</span>
      </div>
    </div>
    <a-modal title="加字" v-model="textDialog" @ok="textAddOk" cancelText="取消" okText="确定" >
      <textarea v-model="text" class="add-text" placeholder="请添加文字" />
      <p style="font-size: 12px; color: #646566; padding: 10px 10px;">
      请注意，您的店招的店名需要和营业执照的名称一致或者是营业执照的名称的缩写
    </p>
    </a-modal>
    <a-modal v-model="tempDialog" @ok="tempDialog = false" :footer="null">
      <div style="padding: 15px 0px">
        <async-image
          width="100%"
          height="100px"
          :style="{ objectFit: 'contain' }"
          :src="work.cover_image_url"
        />
      </div>
    </a-modal>
  </div>
</template>
<script>
import store from "core/mobile/store/index";
import { resolveImgUrl } from "core/support/imgUrl";
import { appUploadMaterialAttachmentOSS } from "core/api/";

import { mapActions, mapState } from "vuex";
import { later, sleep } from "@editor/utils/tool";

export default {
  store,
  computed: {
    ...mapState("editor", {
      elements: (state) => state.editingPage.elements,
      work: (state) => state.work,
    }),
  },
  data() {
    return {
      textDialog: false,
      tempDialog: false,
      text: "",
    };
  },
  props: ["showPicConfirm"],
  methods: {
    resolveImgUrl,
    ...mapActions("editor", [
      "elementManager",
      "setEditingElement",
      "mCreateCover",
      "setPic",
      "changeTokenScreenShotStatus",
    ]),
    beforUploader() {
      this.showPicConfirm().then(() => {
        document.querySelector('.fileUploader input').click()
      });
    },
    async upload(evt) {
      let toast = this.$message.loading("上传中...", 0);
      try {
        const form = new FormData();
        form.append("file", evt.file);
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
      } catch (e) {
        this.$message.error("上传失败", 2000);
      }
      toast();
    },
    async createShopSign() {
      const toast = this.$message.loading("生成中...", 0);
      this.changeTokenScreenShotStatus(true);
      await sleep(1000);
      try {
        const info = await this.mCreateCover({ el: "#content_edit" });
        this.setPic({
          type: "signboardPic",
          value: info.data.urlPath,
        });
        this.$message.success("创建成功", 2);
        later(() => {
          this.$router.push({
            name: "editLive",
          });
        }, 3000);
      } catch (e) {
        console.log(e);
        this.$message.error("创建失败");
      }
      this.changeTokenScreenShotStatus(false);
      toast();
    },
    textAddOk() {
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
      this.textDialog = false;
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
  },
};
</script>
<style lang="scss" scoped>
.flex {
  display: flex;
  align-items: center;
}
.tool-bar {
  justify-content: space-between;
  span {
    font-weight: bold;
    margin-left: 5px;
  }
  .ant-upload {
    margin-left: 0px;
  }
}
.tool-bar__left,
.tool-bar__right {
  div {
    margin-left: 20px;
    cursor: pointer;
  }
}
.tool-bar__right {
  padding-right: 20px;
}
.add-text {
  width: 100%;
  height: 100px;
  border: none;
  outline: none;
  background: #eaeaea;
}
</style>

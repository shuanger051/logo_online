<template>
  <div class="edit-select flex">
    <div class="edit-select__header flex">
      <span class="edit-select__header-title">请选择点击需要处理的项目</span>
    </div>
    <div
      class="edit-select__main flex"
    >
      <div class="edit-select__left"  @click="$router.push(`/signboard/attribute`)">
        <span class="exit-select__txt">菜单式<br />在线设计</span>
        <icon-fa
          icon="fluent:design-ideas-20-regular"
          color="#e98c49"
          width="80%"
        />
      </div>

      <a-upload
        class="edit-select__right"
        name="file"
        :customRequest="upload"
        :showUploadList="false"
      >
        <span class="exit-select__txt">已有设计上传</span>
        <icon-fa
          icon="fa:upload"
          color="#82b6f8"
          width="70%"
          height="70%"
          style="margin-top: 10px"
        />
      </a-upload>
    </div>
  </div>
</template>
<script>
import { sleep } from "@editor/utils/tool";
import { appUploadMaterialAttachmentOSS } from "core/api/";
import { mapActions } from "vuex";
import store from "core/pc/store";

export default {
  store,
  methods: {
    ...mapActions("editor", ["setPic"]),
    async upload({ file }) {
      const toast = this.$message.loading("上传中...", 0);
      const isJpgOrPng =
        file.type === "image/jpeg" || file.type === "image/png";
      if (!isJpgOrPng) {
        this.$message.error("上传格式为jpeg或者png");
        return;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("图片大小不能超过 2MB!");
        return;
      }
      try {
        const form = new FormData();
        form.append("file", file);
        const info = await appUploadMaterialAttachmentOSS(form);
        this.setPic({
          type: "signboardPic",
          value: info.data.urlPath,
        });
        await sleep(1000);

        this.$router.push({
          name: "editLive",
        });
      } catch (e) {}
      toast();
    },
  },
};
</script>
<style lang="scss">
.flex {
  display: flex;
}
.edit-select {
  width: 1000px;
  margin: 0px auto;
  flex-direction: column;
}
.edit-select__header-title {
  flex-grow: 1;
  font-weight: 500;
  font-size: 16px;
  padding: 0 24px;
  border-bottom: 1px solid rgb(235, 235, 235);
  line-height: 48px;
  margin-bottom: 20px;
}
.edit-select__main {
  justify-content: space-around;
}
.edit-select__left,
.edit-select__right {
  background: #efefed;
  border-radius: 20px;
  height: 200px;
  width: 300px;
  position: relative;
  align-items: center;
  .exit-select__txt {
    position: absolute;
    top: 10px;
    right: 20px;
    font-weight: 500;
    font-size: 16px;
  }
  .ant-upload {
    display: flex;
    width: 100%;
    height: 100%;
  }
}
</style>

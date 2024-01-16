<template>
  <div>
    <van-button color="#1989fa" plain @click="show = true" class="btn"
      >素材库</van-button
    >
    <van-uploader
        :after-read="afterRead"
        :max-size="1024 * 1024 * 2"
        @oversize="onOversize"
    >
      <van-button color="#07c160" plain @click="show = true" class="btn"
        >本地上传</van-button
      >
  </van-uploader>
    <van-dialog v-model="show" title="标题" show-cancel-button @confirm = "configHandler">
      <div class="image-container">
        <van-image 
          :src="resolveImgUrl(item.url, true)" 
          fit="contain" 
          width="100"
          height="100" 
          @click = "selectHandler(item)" 
          :class="{'image-item': true, 'active': select && (select.id == item.id) }" 
          v-for="item in imageList" 
        />
      </div>
      <van-pagination
        v-model="page.current"
        :total-items="page.total"
        :items-per-page="10"
      />
    </van-dialog>
  </div>
</template>
<script>
import { resolveImgUrl} from "core/support/imgUrl";
import { appGetMaterialListByPageApiOSS, appUploadMaterialAttachmentOSS } from "core/api/";
import { Toast } from "vant";

export default {
  data() {
    return {
      show: false,
      imageList: [],
      page: {
        current: 1,
        total: 0,
      },
      select: null,
    };
  },
  watch: {
    "page.current": {
      handler() {
        this.getList();
      },
      immediate: true,
    },
  },
  methods: {
    resolveImgUrl,
    selectHandler(item) {
      this.select = item;
    },
    configHandler() {
      if (this.select) {
        this.$emit('input', this.select.url)
      }
    },
    async afterRead(file) {
      const toast = Toast.loading({
        message: "上传中",
        forbidClick: true,
        duration: 0,
      });
      const form = new FormData()
      form.append('file', file.file)
      const info = await appUploadMaterialAttachmentOSS(form);
      const url = info.data.urlPath
      this.$emit('input', url)
      toast.clear();
    },
    onOversize(file) {
      Toast("文件大小不能超过 2M");
    },
    async getList() {
      const res = await appGetMaterialListByPageApiOSS({
        pageNum: this.page.current,
      });
      if (!res) {
        return 
      }
      const data = res.data;
      this.page.total = data.total;
      const list = data.list.map((item) => {
        return {
          id: item.id,
          url: item.urlPath,
        };
      });
      this.imageList = list;
    },
  },
};
</script>
<style scoped lang="scss">
.image-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  .active {
    border: 1px solid #1989fa!important;
  }
  .image-item {
    margin-bottom: 10px;
    border: 1px solid #ebedf0;
  }
}
.btn {margin-right: 10px;}

</style>

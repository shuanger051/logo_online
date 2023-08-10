<template>
  <div>
    <van-button color="#7232dd" plain @click="show = true" class="btn"
      >更换图片</van-button
    >
    <van-dialog v-model="show" title="标题" show-cancel-button @confirm = "configHandler">
      <div class="image-container">
        <van-image 
          :src="resolveImgUrl(item.url)" 
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
import { resolveImgUrl } from "core/support/imgUrl";
import { appGetMaterial } from "core/api/";

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
    async getList() {
      const res = await appGetMaterial({
        pageNum: this.page.current,
      });
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

</style>

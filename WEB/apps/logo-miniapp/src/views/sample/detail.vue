<template>
  <div class="page-wrap">
    <van-row v-if="detail">
      <van-col v-for="(val, idx) in detail.imgs" :key="idx" :span="24">
        <van-image :src="val" />
      </van-col>
    </van-row>
    <van-empty v-else image="search" description="未找到相关内容" />
  </div>
</template>
<script>
export default {
  data() {
    return {
      detail: null,
    };
  },
  created() {
    const { name, type } = this.$route.query;
    const list = window.pageContentJson.material[type];
    const data = list.find((item) => item.key == name);
    if (data) this.detail = data.content;
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px;
}
</style>

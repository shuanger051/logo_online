<template>
  <div class="page-wrap">
    <a-row :gutter="12" v-if="detail">
      <!-- 图片 -->
      <a-col :span="8" v-for="(val, idx) in detail.imgs" :key="idx">
        <img class="example-img" :src="val" fit="cover" />
      </a-col>
    </a-row>
    <a-empty v-else description="未找到相关内容" />
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
  padding-top: 24px;
  font-size: 14px;
  max-width: 1000px;
  margin: 0 auto;
  line-height: 1.6em;
  .example-img {
    max-width: 100%;
  }
}
</style>

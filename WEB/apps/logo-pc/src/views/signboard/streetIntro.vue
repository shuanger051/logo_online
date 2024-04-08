<template>
  <div class="page-wrap">
    <a-row :gutter="12">
      <a-col :span="12">
        <img fit="cover" src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </a-col>
      <a-col :span="12">
        <img fit="cover" src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </a-col>
      <a-col :span="12">
        <img fit="cover" src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </a-col>
    </a-row>
    <div class="action-bar">
      <a-button type="primary" @click="onNext">下一步</a-button>
    </div>
  </div>
</template>
<script>
export default {
  methods: {
    onNext() {
      const { query } = this.$route;
      this.$router.push({
        path: "/signboard/attribute",
        query,
      });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  :deep(.ant-col) {
    margin-bottom: 12px;
    img {
      width: 100%;
    }
  }

  .action-bar {
    margin-top: 24px;
  }
}
</style>

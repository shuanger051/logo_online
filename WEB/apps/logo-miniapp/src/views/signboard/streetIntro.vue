<template>
  <div class="page-wrap">
    <van-row>
      <van-col :span="24">
        <van-image src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </van-col>
      <van-col :span="24">
        <van-image src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </van-col>
      <van-col :span="24">
        <van-image src="https://img01.yzcdn.cn/vant/cat.jpeg" />
      </van-col>
    </van-row>
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
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
  padding: 24px 12px 64px;
}
</style>

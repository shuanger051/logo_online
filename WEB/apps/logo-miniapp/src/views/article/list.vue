<template>
  <div class="page-wrap">
    <van-list :loading="loading">
      <van-cell
        v-for="item in list"
        :to="`/article/${item.channelId}/detail?pid=${item.id}`"
        :key="item.id"
        :title="item.title"
        :label="item.description"
      >
        <van-image :src="item.imgUrl" />
      </van-cell>
    </van-list>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      list: [
        {
          id: "1",
          channelId: "1",
          title: "测试",
          description: "测试摘要",
          imgUrl: "https://img01.yzcdn.cn/vant/cat.jpeg",
        },
        {
          id: "3",
          title: "测试测试测试测试测试",
          channelId: "1",
          description: "测试摘要测试摘要测试摘要测试摘要测试摘要测试摘要测",
          imgUrl: "https://img01.yzcdn.cn/vant/cat.jpeg",
        },
      ],
    };
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0;
  background-color: @gray-2;
  // min-height: 100vh;
  :deep(.van-list) {
    .van-cell {
      &__value {
        line-height: 0;
      }
    }
    .van-image {
      height: 64px;
    }
  }
}
</style>

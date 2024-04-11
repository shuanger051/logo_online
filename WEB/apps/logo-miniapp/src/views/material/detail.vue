<template>
  <div class="page-wrap">
    <van-row gutter="12" v-if="detail">
      <!-- 文字 -->
      <van-col :span="24" style="margin-bottom: 12px;">
        {{ detail.text }}
      </van-col>
      <!-- 图片 -->
      <van-col :span="12" v-for="(val, idx) in detail.imgs" :key="idx">
        <van-image :src="val" fit="cover" />
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
    const { name } = this.$route.query;
    const list = window.pageContentJson.texture;
    const data = list.find((item) => (item.key == name));
    if (data) this.detail = data.content;
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px;
  font-size: 14px;
  line-height: 1.6em;
}
</style>

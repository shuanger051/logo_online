<template>
  <div class="page-wrap">
    <van-panel title="测试">
      <van-grid>
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
      </van-grid>
    </van-panel>
    <van-panel title="测试">
      <van-grid>
        <van-grid-item text="文章测试" to="/article/1/list" />
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
      </van-grid>
    </van-panel>
    <van-panel title="测试">
      <van-grid>
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
      </van-grid>
    </van-panel>
  </div>
</template>

<script>
// @ is an alias to /src
export default {
  name: "Home",
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0;
  min-height: 100vh;
  background-color: @gray-2;
  :deep(.van-panel) {
    &:not(:last-child) {
      margin-bottom: 12px;
    }
  }
}
</style>

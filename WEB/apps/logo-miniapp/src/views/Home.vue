<template>
  <div class="page-wrap">
    <van-panel title="测试">
      <van-grid>
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
        <van-grid-item text="测试去" />
      </van-grid>
    </van-panel>
    <van-panel title="模板测试">
      <van-grid>
        <van-grid-item text="模板测试35" to="/editTemplate/35" />
        <van-grid-item text="模板测试36" to="/editTemplate/36"/>
        <van-grid-item text="模板测试37" to="/editTemplate/37"/>
        <van-grid-item text="模板测试38" to="/editTemplate/38"/>
        <van-grid-item text="模板测试39" to="/editTemplate/39"/>
        <van-grid-item text="模板测试40" to="/editTemplate/40"/>
        <van-grid-item text="模板测试41" to="/editTemplate/41"/>
        <van-grid-item text="模板测试42" to="/editTemplate/42"/>
        <van-grid-item text="模板测试43" to="/editTemplate/43"/>
        <van-grid-item text="模板测试44" to="/editTemplate/44"/>
        <van-grid-item text="模板测试45" to="/editTemplate/45"/>
        <van-grid-item text="模板测试46" to="/editTemplate/46"/>

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
  span {
    font-size: 14px;
  }
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

<template>
  <div class="page-wrap">
    <van-panel title="常用功能">
      <van-grid>
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="xinxigongkainianbao"
          text="我的商铺"
          to="/shop/list"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhengfuxinxigongkaishenqing"
          text="店招备案"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhengfuxinxigongkaizhinan"
          text="备案进度"
        />
      </van-grid>
    </van-panel>
    <van-panel title="模板测试">
      <van-grid>
        <van-grid-item text="模板测试35" to="/editTemplate/35" />
        <van-grid-item text="模板测试36" to="/editTemplate/36" />
        <van-grid-item text="模板测试37" to="/editTemplate/37" />
        <van-grid-item text="模板测试38" to="/editTemplate/38" />
        <van-grid-item text="模板测试39" to="/editTemplate/39" />
        <van-grid-item text="模板测试40" to="/editTemplate/40" />
        <van-grid-item text="模板测试41" to="/editTemplate/41" />
        <van-grid-item text="模板测试42" to="/editTemplate/42" />
        <van-grid-item text="模板测试43" to="/editTemplate/43" />
        <van-grid-item text="模板测试44" to="/editTemplate/44" />
        <van-grid-item text="模板测试45" to="/editTemplate/45" />
        <van-grid-item text="模板测试46" to="/editTemplate/46" />
      </van-grid>
    </van-panel>
    <van-panel title="备案信息">
      <van-grid>
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhishiku"
          text="法律法规"
          to="/article/1/list"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhengfuxinxigongkaizhinan"
          text="备案流程"
        />
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
    &__header {
      .van-cell__title {
        &::before {
          content: "";
          display: inline-block;
          height: 10px;
          width: 2px;
          margin-right: 8px;
          background-color: @blue;
        }
      }
    }
  }
  :deep(.van-grid) {
    &-item__icon {
      font-size: 22px;
      display: inline-block;
      width: 32px;
      height: 32px;
      line-height: 32px;
      text-align: center;
      border-radius: 100%;
      background-color: @blue;
      color: @white;
    }
  }
}
</style>

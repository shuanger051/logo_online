<template>
  <div class="page-wrap">
    <van-panel title="店招承诺备案">
      <van-grid>
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhengfuxinxigongkaishenqing"
          text="商铺登记"
          to="/shop/detail"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="xinxigongkainianbao"
          text="我的商铺"
          to="/shop/list"
        />
      </van-grid>
    </van-panel>
    <van-panel title="备案信息">
      <van-grid>
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhishiku"
          text="法律法规"
          to="/article/3/detail?pid=486"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="zhengfuxinxigongkaizhinan"
          text="备案流程"
          to="/article/1/detail?pid=482"
        />
        <van-grid-item
          icon-prefix="iconfont icon"
          icon="fadingzhudonggongkaineirong"
          text="备案信息公告"
          to="/article/4/list"
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

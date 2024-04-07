<template>
  <div class="page-wrap">
    <van-panel title="材质类型">
      <van-grid :column-num="2" :border="false" direction="horizontal">
        <van-grid-item icon="photo-o" text="金属材质" to="/material/detail?type=" />
        <van-grid-item icon="photo-o" text="亚格力材质" to="/material/detail?type="  />
        <van-grid-item icon="photo-o" text="木质材质" to="/material/detail?type="  />
        <van-grid-item icon="photo-o" text="其他材质" to="/material/detail?type="  />
      </van-grid>
    </van-panel>
  </div>
</template>
<style lang="less" scoped>
.page-wrap{
  box-sizing: border-box;
  padding: 24px 12px 64px;
  background-color: @gray-2;
  height: 100%;
  :deep(.van-panel) {
    margin-bottom: 12px;
    border-radius: 8px;
    overflow: hidden;
    &__header {
      position: relative;
      line-height: 24px;
      font-size: 16px;
      &::before {
        content: "";
        display: inline-block;
        margin-right: 8px;
        transform: translateY(5px);
        width: 4px;
        height: 14px;
        background-color: @blue;
      }
    }
    &__content {
      padding: 12px 0;
    }
  }
}
</style>
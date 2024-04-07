<template>
  <div class="page-wrap">
    <van-panel title="街区类型">
      <van-grid :column-num="2" :border="false" direction="horizontal">
        <van-grid-item
          icon="photo-o"
          text="一般街区"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="photo-o"
          text="商业街区"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="photo-o"
          text="特色街区"
          to="/sample/detail?type="
        />
      </van-grid>
    </van-panel>
    <van-panel title="效果类型">
      <van-grid :column-num="2" :border="false" direction="horizontal">
        <van-grid-item
          icon="photo-o"
          text="实景素材"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="photo-o"
          text="效果素材"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="photo-o"
          text="平面素材"
          to="/sample/detail?type="
        />
      </van-grid>
    </van-panel>
  </div>
</template>

<style lang="less" scoped>
.page-wrap {
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

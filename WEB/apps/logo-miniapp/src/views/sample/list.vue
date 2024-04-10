<template>
  <div class="page-wrap">
    <van-panel title="街区类型">
      <van-grid
        class="street-grid"
        :column-num="2"
        :border="false"
        :center="false"
        direction="horizontal"
      >
        <van-grid-item
          icon="jiequjingjigongxiandufenxi"
          icon-prefix="iconfont icon"
          text="一般街区"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="shangye"
          icon-prefix="iconfont icon"
          text="商业街区"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="tesefuwu"
          icon-prefix="iconfont icon"
          text="特色街区"
          to="/sample/detail?type="
        />
      </van-grid>
    </van-panel>
    <van-panel title="效果类型">
      <van-grid
        :column-num="2"
        :border="false"
        :center="false"
        direction="horizontal"
      >
        <van-grid-item
          icon="sucai"
          icon-prefix="iconfont icon"
          text="实景素材"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="sucai"
          icon-prefix="iconfont icon"
          text="效果素材"
          to="/sample/detail?type="
        />
        <van-grid-item
          icon="sucai"
          icon-prefix="iconfont icon"
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
  min-height: 100%;
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("../../assets/app-footer-bg.png");
    background-repeat: no-repeat;
    background-position: bottom center;
    background-size: 115% auto;
    background-attachment: fixed;
    filter: opacity(40%);
  }
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

  :deep(.van-grid) {
    &.street-grid {
      .van-grid-item__icon {
        font-size: 19px;
        &.icon-shangye {
          color: #ae00ff;
        }
        &.icon-jiequjingjigongxiandufenxi {
          color: #36ddfc;
        }
        &.icon-tesefuwu {
          color: #fe9500;
        }
      }
    }
    &-item {
      padding-left: 24px;
      &:nth-child(4n + 1) {
        .van-grid-item__icon {
          color: #de8f30;
        }
      }
      &:nth-child(4n + 2) {
        .van-grid-item__icon {
          color: #d1ccc5;
        }
      }
      &:nth-child(4n + 3) {
        .van-grid-item__icon {
          color: #b09076;
        }
      }
      &:nth-child(4n) {
        .van-grid-item__icon {
          color: #6d90ad;
        }
      }
      &__content {
        align-items: center;
      }
    }
    &-item__text {
      font-size: 14px;
    }
    &-item__icon {
      font-size: 16px;
    }
  }
}
</style>

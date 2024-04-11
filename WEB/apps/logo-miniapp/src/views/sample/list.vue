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
          v-for="item in streetArr"
          :key="item.key"
          :text="item.name"
          :to="`/sample/detail?type=street&name=${item.key}`"
        >
          <van-icon
            slot="icon"
            class-prefix="iconfont icon"
            :name="item.icon"
            :color="item.iconColor"
          />
        </van-grid-item>
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
          v-for="item in designArr"
          :key="item.key"
          :text="item.name"
          :to="`/sample/detail?type=design&name=${item.key}`"
        >
          <van-icon
            slot="icon"
            class-prefix="iconfont icon"
            :name="item.icon"
            :color="item.iconColor"
          />
        </van-grid-item>
      </van-grid>
    </van-panel>
  </div>
</template>
<script>
export default {
  data() {
    const {
      street: streetArr,
      design: designArr,
    } = window.pageContentJson.material;
    return {
      streetArr,
      designArr,
    };
  },
};
</script>

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
    &-item {
      padding-left: 24px;
      &__content {
        align-items: center;
      }
      &__icon-wrapper {
        margin-right: 8px;
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

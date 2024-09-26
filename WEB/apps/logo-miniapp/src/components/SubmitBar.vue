<template functional>
  <div class="submit-bar">
    <div class="submit-bar__tips" v-if="slots().tips">
      <slot name="tips"></slot>
    </div>
    <div class="submit-bar__content">
      <slot></slot>
    </div>
  </div>
</template>
<style lang="less" scoped>
.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  box-sizing: border-box;
  user-select: none;
  box-shadow: 0 -2px 64px 8px rgba(100, 100, 100, 0.1);
  background-color: @white;
  padding-bottom: calc(20px + constant(safe-area-inset-bottom)); /* 兼容 iOS < 11.2 */
  padding-bottom: calc(20px + env(safe-area-inset-bottom)); /* 兼容 iOS >= 11.2 */
  &__tips {
    padding: 8px 12px;
    font-size: 12px;
    line-height: 2em;
  }
  &__content {
    :deep(.van-button) {
      &:only-child {
        border-color: @gray-2;
      }
    }
  }
}
</style>

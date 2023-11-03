<template>
  <div class="page-suspend">
    <div class="page-item page-prev" @click="onPrev">
      <van-icon name="arrow-up" />
    </div>
    <div class="page-item page-size">
      <div class="van-hairline--bottom">{{ current }}</div>
      <div>{{ totalPage }}</div>
    </div>
    <div class="page-item page-next" @click="onNext">
      <van-icon name="arrow-down" />
    </div>
  </div>
</template>
<script>
export default {
  props: {
    // 总页码
    total: {
      type: [Number, String],
      default: 0,
    },
    // 每页条数
    size: {
      type: [Number, String],
      default: 30,
    },
    // 当前页码
    current: {
      type: [Number, String],
      default: 1,
    },
  },
  computed: {
    // 总页数
    totalPage() {
      const { total, size } = this;
      return Math.ceil(total / size);
    },
  },
  methods: {
    // 上一页
    onPrev() {
      const { current } = this;
      if (current > 0) this.$emit("change", { current: current - 1 });
    },
    // 下一页
    onNext() {
      const { current } = this;
      if (current < this.totalPage)
        this.$emit("change", { current: current + 1 });
    },
  },
};
</script>
<style lang="less" scoped>
.page-suspend {
  position: fixed;
  bottom: 30px;
  right: 12px;
  .page-item {
    background-color: @white;
    text-align: center;
    padding: 4px;
    width: 32px;
    height: 28px;
    font-size: 12px;
    color: @gray-7;
    box-sizing: border-box;
    box-shadow: 0 0 38px 8px rgba(0, 0, 0, 0.28);
    border-radius: 4px;
    &:not(:last-child) {
      margin-bottom: 4px;
    }
    &.page-prev {
      line-height: 22px;
      border-top-left-radius: 50%;
      border-top-right-radius: 50%;
    }
    &.page-next {
      line-height: 22px;
      border-bottom-left-radius: 50%;
      border-bottom-right-radius: 50%;
    }
    &.page-size {
      padding-top: 0;
      padding-bottom: 0;
      height: auto;
      line-height: 24px;
      & > div {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      :deep(.van-hairline--bottom) {
        &::after {
          border-color: @gray-5;
        }
      }
    }
  }
}
</style>

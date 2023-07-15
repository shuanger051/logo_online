<template>
  <div class="title-bar-line"
    :style="{'margin-bottom': !showBorderBottom ? '10px' : '20px'}"
    :class="showBack && showBorderBottom ? 'border-bottom' : ''">
    <span class="title ellipsis">{{title || ''}}</span>
    <div class="back-wrapper"
          v-if="showBack"
          @click="back()">
      <h-icon name="return"
              color="#666"
              size="10"></h-icon>
      <span>返回</span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'titleBar',
  props: {
    title: String,
    showBack: {
      type: Boolean,
      default: false
    },
    showBorderBottom: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    back () {
      this.$emit('back')
    }
  }
}
</script>
<style lang="scss" scoped>
.title-bar-line {
  width: 100%;
  height: 20px;
  position: relative;
  display: flex;
  margin-top: 16px;
  .title {
    display: inline-block;
    font-family: PingFangSC-Semibold;
    line-height: 18px;
    font-size: 12px;
    font-weight: 600;
    color: #333;
    padding-left: 8px;
    margin-left: 16px;
    flex: 1;
    border-left: 4px solid #037df3;
  }
  .back-wrapper {
    height: 20px;
    line-height: 18px;
    margin-right: 16px;
    &:hover {
      cursor: pointer;
    }
  }
  .ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
}
.border-bottom:after {
  content: '';
  width: 100%;
  height: 1px;
  top: 30px;
  position: absolute;
  background-color: #f2f2f2;
}
</style>

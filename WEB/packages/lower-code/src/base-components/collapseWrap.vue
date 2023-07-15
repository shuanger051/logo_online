<template>
  <div style="margin-top:10px;">
    <div class="coll-top-container">
      <div class="name-container" :style="{ borderLeftWidth: borderLeftWidth + 'px'}">
        {{ name }}
        <h-tooltip :content="tips" placement="top" transfer v-if="tips">
          <h-icon name="help">?</h-icon>
        </h-tooltip>
      </div>
      <div class="icon-div" @click="handleCollapse">
        <div  v-if="collapse"><h-icon name="double arrow icon-doublearrow" :size="12"></h-icon></div>
        <div  v-else style="transform: rotate(180deg);"><h-icon name="double arrow icon-doublearrow" :size="12"></h-icon></div>
      </div>
    </div>
    <transition>
      <div v-show="!collapse" :style="`padding-left: ${contentPaddingLeft}px; ${defaultHeightPercentage? `height: ${defaultHeightPercentage}%;` : '' } ${extraStyle}`">
        <slot></slot>
      </div>
    </transition>
  </div>
</template>
<script>
export default {
  name: 'collapseWrap',
  props: {
    tips: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    },
    hideBar: {
      type: Boolean,
      default: false
    },
    contentPaddingLeft: {
      type: Number,
      default: 12
    },
    defaultHeightPercentage: {
      type: Number
    },
    hiddenCollapse: {
      type: Boolean,
      default: () => false
    }, // 默认是否不显示具体内容 false为显示， true为不显示
    extraStyle: {
      type: String,
      default: () => ''
    } // 额外的样式
  },
  data() {
    return {
      collapse: false,
      borderLeftWidth: this.hideBar ? 0 : 4
    }
  },
  created () {
    this.collapse = this.hiddenCollapse
  },
  methods: {
    handleCollapse() {
      this.collapse = !this.collapse
    }
  }
}
</script>
<style scoped lang="scss">
.coll-top-container {
  position: relative;
  margin: 12px 0;
  .name-container {
    position: relative;
    display: inline-block;
    font-size: 12px;
    font-weight: 600;
    height: 16px;
    line-height: 16px;
    color: #333;
    padding: 0 8px;
    border-left-color: #037DF3;
    border-left-style: solid;
    background-color: #fff;
  }
  &:before {
    content: '';
    position: absolute;
    display: block;
    width: 100%;
    height: 1px;
    background: transparent;
    border-top: 1px dashed #ddd;
    top: 9px;
    left: 0;
  }
  .icon-div {
    /* display: inline-block; */
    background-color: #fff;
    cursor: pointer;
    float: right;
    position: relative;
    right: 0;
    top: 0;
  }
}
</style>

<template>
  <div :class="classObject">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'hDivider',
  props: {
    direction: {
      type: String,
      default: 'horizontal'
    }
  },
  computed: {
    classObject: function () {
      return {
        'h-divider': true,
        'h-divider-horizontal': this.direction === 'direction',
        'h-divider-vertical': this.direction === 'vertical'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.h-divider {
  background-color: #dcdfe6;
  position: relative;
}

.h-divider-horizontal {
  display: block;
  height: 1px;
  width: 100%;
  margin: 24px 0;
}

.h-divider-vertical {
  display: inline-block;
  width: 1px;
  height: 1em;
  margin: 0 8px;
  vertical-align: middle;
  position: relative;
}
</style>

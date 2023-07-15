<template>
  <div class="ruler-line-container">
    <div class="horizontal-line-wrap">
      <div v-for="(item, index) in horizontalArr" :key="index" class="calibration" :class="{ 'high-line': index %2 == 0, 'low-line': index %2 != 0 }">
        <span v-if="index %2 == 0"> {{ item }}</span>
      </div>
    </div>
    <div class="vertical-line-wrap">
      <div v-for="(item, index) in verticalArr" :key="index" class="calibration" :class="{ 'high-line': index %2 == 0, 'low-line': index %2 != 0 }">
        <span v-if="index %2 == 0"> {{ item }}</span>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'

export default {
  name: 'rulerLine',
  data() {
    return {
    }
  },
  props: {
    horizontalArr: {
      type: Array,
      default: () => []
    },
    verticalArr: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    ...mapState('cms/works', [
      'width', 'height'
    ])
  },
  methods: {

  }
}
</script>
<style lang="scss" scoped>
.ruler-line-container {
  position: absolute;
  top:0;
  left:0;
  width:375px;
}
.horizontal-line-wrap {
  position: absolute;
  display: flex;
  align-items: flex-end;
  top: -16px;
  /* left: 1; */
  /* height: 0; */
  .calibration {
    float: left;
    width: 1px;
    margin-left: 49px;
    border-left: 1px solid #a3afb7;
    box-sizing: border-box;
    &:first-child {
      margin-left: 1px;
    }
    span {
      margin-left: 5px;
      line-height: 1;
    }
  }
  .low-line {
    height: 4px;
  }
  .high-line {
    height: 16px;
  }
}
.vertical-line-wrap {
  display: flex;
  align-items: flex-start;
  text-align: left;
  width: 20px;
  transform-origin: left top;
  transform: rotate(90deg);
  display: absolute;
  top: -6;
  left: 1;
  .calibration {
    float: left;
    display: block;
    height: 12px;
    width: 1px;
    margin-left: 49px;
    border-left: 1px solid #a3afb7;
    box-sizing: border-box;
    &:first-child {
      margin-left: 1px;
    }
    span {
      margin-left: 5px;
      line-height: 1;
    }
  }
  .low-line {
    height: 4px;
  }
  .high-line {
    height: 16px;
  }
}
</style>

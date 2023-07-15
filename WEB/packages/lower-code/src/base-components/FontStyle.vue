<template>
  <div class="font-panel">
    <h-radio-group type="button" size="small" v-model="fontWeight"  @on-change="updateClick('font-weight', fontWeight)">
      <h-radio :canCancel="true" label="bold"
        ><span class="iconfont icon-jiacu" title="加粗"></span
      ></h-radio>
    </h-radio-group>
    <h-radio-group type="button" class="panel-radio-group" size="small" v-model="fontStyle" @on-change="updateClick('font-style', fontStyle)">
      <h-radio :canCancel="true" label="italic">
        <span class="iconfont icon-qingxie-" title="倾斜"></span>
      </h-radio>
    </h-radio-group>
    <h-radio-group type="button" class="panel-radio-group" size="small" v-model="textDecoration" @on-change="updateClick('text-decoration', textDecoration)">
      <h-radio :canCancel="true" label="underline"
        ><span class="iconfont icon-ziyuan" title="下划线"></span
      ></h-radio>
    </h-radio-group>
    <h-radio-group type="button" class="panel-radio-group" size="small" v-model="textDecoration"  @on-change="updateClick('text-decoration', textDecoration)">
      <h-radio :canCancel="true" label="line-through"
        ><span class="iconfont icon-strikethrough" title="删除线"></span
      ></h-radio>
    </h-radio-group>
  </div>
</template>
<script>
export default {
  props: {
    fontWeight: {
      type: String,
      default: () => ''
    }, // 加粗
    fontStyle: {
      type: String,
      default: () => ''
    }, // 字体样式
    textDecoration: {
      type: String,
      default: () => ''
    } // 下划线、删除线
  },
  watch: {

  },
  created () {
  },
  data() {
    return {
    }
  },
  methods: {
    updateClick(type, value) {
      const data = {
        name: type,
        value: value
      }
      this.$emit('updateFunc', data)
    }
  }
}
</script>
<style lang="scss" scoped>
// 文本样式
/deep/ .panel-radio-group {
  margin-left: -3.5px;
}
.font-panel {
  /deep/ .h-radio-wrapper.h-radio-group-item {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 28px;
    height: 28px;
    padding: 0;
  }
  .iconfont {
    font-size: 18px;
    margin: 0;
  }
}
.font-desc {
  text-align: center;
  margin: 0 0 8px 0;
}
.h-col-style {
  text-align: center;
}
</style>

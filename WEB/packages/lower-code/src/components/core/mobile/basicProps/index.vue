<template>
  <div class="props-wrap">
    <div v-for="(item) in formItems" :key="item.propsName" :style="item.config.itemStyle || {}">
  <van-field name="stepper" :label="item.config.showLable ? item.config.label : ''">
    <template #input>
      <component
        :is="item.type"
        v-model="formModel[item.propsName]"
        v-bind="item.props"
      ></component>
      </template>
      </van-field>
    </div>
  </div>
</template>

<script>
export default {
  props: ["formItems", "formModel"],
  mounted() {
    console.log(this.formItems, 8888)
  }
};
</script>
<style lang="scss">
  .props-wrap {
    display: flex;
    flex-wrap: wrap;
    justify-content: start;
    align-items: center;
    padding-top: 30px;
    > div {
      margin-bottom: 10px;
      margin-left: 10px;
    }
    .van-field {
      padding: 0px;
      align-items: center;
    }
    .van-cell {
      overflow:visible;
    }
    .van-field__label {
      width: auto;
    }
  }

 .el-color-picker__panel {
  z-index: 10000!important;
 }
</style>
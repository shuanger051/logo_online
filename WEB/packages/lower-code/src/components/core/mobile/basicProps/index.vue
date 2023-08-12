<template>
  <div class="props-wrap">
    <div v-for="(item) in formItems" :key="item.propsName">
  <van-field name="stepper" :label="item.config.showLable ? item.config.label : ''">
    <template #input>
      <component
        :is="item.type"
        v-model="formModel[item.propsName]"
        v-bind="item.props"
        label="测试"
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
  }
};
</script>
<style lang="scss" scoped>
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
    :deep(.van-field) {
      padding: 0px;
      align-items: center;
    }
    :deep(.van-field__label) {
      width: auto;
    }
  }
</style>
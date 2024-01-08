<template>
  <div class="props-wrap">
    <a-form>
      <a-form-item
        v-for="item in formItems"
        :key="item.propsName"
        :label="item.config.showLable ? item.config.label : ''"
        :labelCol="{ span: 7 }"
        :wrapperCol = "{ span: 15, offset: 1 }"
      >
        <component
          :is="item.type"
          v-model="formModel[item.propsName]"
          v-bind="item.props"
        ></component>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  props: ["formItems", "formModel"],
  mounted() {},
};
</script>
<style lang="scss">
.props-wrap {
  .ant-form-item {
    margin-bottom: 14px;
  }
}
</style>

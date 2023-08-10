<template>
  <div class="props-wrap">
    <div v-for="(item) in formItems" :key="item.propsName">
      <component
        :is="item.type"
        v-model="formModel[item.propsName]"
        v-bind="item.props"
      ></component>
    </div>
  </div>
</template>

<script>
export default {
  props: ["formItems", "formModel"],
};
</script>
<style lang="scss" scoped>
  .props-wrap {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    align-items: center;
    padding-top: 30px;
    > div {
      margin-bottom: 10px;
    }

  }
</style>
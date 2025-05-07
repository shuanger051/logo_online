<template>
  <a-select v-model="innerVal" v-bind="$attrs" placeholder="请选择">
    <a-select-option
      v-for="item in options"
      :value="item.value"
      :label="item.label"
      :key="item.value"
      option-label-prop="label"
    />
  </a-select>
</template>
<script>
export default {
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: [Array, String, Number],
    },
    // 选项
    options: {
      type: Array,
      default: () => [],
    },
    placeholder: {
      type: String,
      default: "请选择",
    },
  },
  computed: {
    innerVal: {
      set(val) {
        this.$emit("change", val);
      },
      get() {
        return this.value;
      },
    },
  },
};
</script>

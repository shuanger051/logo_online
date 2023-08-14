<template>
  <van-field
    v-bind="fieldProps"
    arrow-direction="down"
    :value="value | formatter(mapDict)"
    :is-link="true"
    :readonly="true"
    @click-input="onShow"
  >
    <van-popup
      slot="extra"
      get-container="body"
      position="bottom"
      v-model="show"
    >
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="onCancel"
        @confirm="onConfirm"
      />
    </van-popup>
  </van-field>
</template>
<script>
import { Field } from "vant";
import { computed, ref } from "vue";
export default {
  model: {
    prop: "value",
    event: "input",
  },
  name: "FieldPicker",
  props: Object.assign(
    {
      columns: {
        type: Array,
        default: () => [],
      },
    },
    Field.props
  ),
  filters: {
    formatter(val, dict) {
      return dict[val];
    },
  },
  setup(props, ctx) {
    const show = ref(false);
    const { attrs, emit } = ctx;

    // 字典项对象
    const mapDict = computed(() => {
      const { columns } = props;
      return columns.reduce((dtm, item) => {
        const { text, value } = item;
        dtm[value] = text;
        return dtm;
      }, {});
    });

    function onShow() {
      show.value = true;
    }

    function onCancel() {
      show.value = false;
    }

    function onConfirm(data) {
      emit("input", data.value);
      show.value = false;
    }

    return {
      show,
      mapDict,
      fieldProps: props,
      pickerProps: attrs,
      // event
      onShow,
      onCancel,
      onConfirm,
    };
  },
};
</script>

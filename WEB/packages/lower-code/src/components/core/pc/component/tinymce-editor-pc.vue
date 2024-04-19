<template>
  <div>
    <editor v-bind="$attrs" @input="input"> </editor>
    <div style="font-size: 12px; color: #646566; line-height: 24px; padding: 10px 0; width: 260px">
      请注意，您的店招的店名需要和营业执照的名称一致或者是营业执照的名称的缩写
    </div>
  </div>
</template>
<script>
import Editor from "@tinymce/tinymce-vue";

export default {
  components: {
    Editor,
  },
  methods: {
    input(v) {
      this.$emit("input", v);
    },
  },
};
</script>

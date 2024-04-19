<template>
  <div>
    <editor v-bind="$attrs" @input="input"> </editor>
    <p style="font-size: 0.3rem; color: #646566; padding: 0 10px;">
      请注意，您的店招的店名需要和营业执照的名称一致或者是营业执照的名称的缩写
    </p>
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

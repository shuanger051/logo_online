<template>
  <div>
    <a-button size="large" @click="openBefore">更换</a-button>
    <upload-dialog
      ref="uploadDialog"
      @selectHandler="$listeners.input"
    ></upload-dialog>
  </div>
</template>
<script>
import UploadDialog from "./uploadDialog";

export default {
  components: {
    UploadDialog,
  },
  props: ['beforeRead'],
  methods: {
    openBefore() {
      if (this.beforeRead) {
        this.beforeRead().then(() => this.open());
      } else {
        this.open();
      }
    },
    open() {
      this.$refs.uploadDialog.showHandler(true);
    },
  },
};
</script>
<style scoped lang="scss"></style>

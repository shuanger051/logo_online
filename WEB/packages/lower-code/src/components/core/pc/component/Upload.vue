<template>
  <div>
    <a-button size="large" @click="open">更换</a-button>
    <upload-dialog ref="uploadDialog" @selectHandler="$listeners.input"></upload-dialog>
  </div>
</template>
<script>
import UploadDialog from './uploadDialog';

export default {
  components: {
    UploadDialog
  },
  methods: {
    open() {
      this.$refs.uploadDialog.showHandler(true)
    }
  }
};
</script>
<style scoped lang="scss"></style>

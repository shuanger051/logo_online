<template>
  <div id="edit-wrap--contaner">
   <edit-wrap />
  </div>
</template>

<script>
import EditWrap from "core/pc/views/editWrap";

export default {
  components: { EditWrap },
};
</script>
<style lang="scss" scoped>
#edit-wrap--contaner {
  padding: 20px 0px;
  height: calc(100vh);
  background: #f1f1f1;
}
</style>
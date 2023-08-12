<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <a-row :gutter="12">
      <!-- 字典项 -->
      <a-col :span="12"><dict-table :selected.sync="selected" /></a-col>
      <!-- 字典子项列表 -->
      <a-col :span="12"><dict-item-table :dict-key="selected" /> </a-col>
    </a-row>
  </div>
</template>
<script>
import DictTable from "./dictTable.vue";
import DictItemTable from "./dictItemTable.vue";
import { mapState } from "vuex";
export default {
  components: { DictItemTable, DictTable },
  data() {
    return {
      // 选中的字典项
      selected: null,
    };
  },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
  },
};
</script>
<style lang="less" scoped></style>

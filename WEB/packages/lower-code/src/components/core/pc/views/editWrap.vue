<template>
  <div class="edit-wrap">
    <div class="edit-wrap--left">
      <props-panel></props-panel>
     
    </div>
    <div class="edit-wrap--main">
      <tool-bar class="edit-wrap-main--top"></tool-bar>
      <edit-panel :elements="elements" :style="getEditStyle()"></edit-panel>
    </div>
  </div>
</template>
<script>
import toolBar from "./toolBar";
import editPanel from "./editPanel";
import propsPanel from "./propsPanel";
import { mapState, mapActions } from "vuex";
import store from "core/pc/store/index";
export default {
  store,
  components: {
    toolBar,
    editPanel,
    propsPanel,
  },
  computed: {
    ...mapState("editor", {
      editingElement: (state) => state.editingElement,
      elements: (state) => state.editingPage.elements,
      work: (state) => state.work,
    }),
  },
  methods: {
    ...mapActions("editor", [
      "fetchWork",
      "setEditingPage",
      "mCreateCover",
      "setEditingElement",
    ]),
    async initEdit() {
      if (this.$route.params.id) {
        const toast = this.$message.loading("加载中...", 0);
        await this.fetchWork({ id: this.$route.params.id });
        toast();
      }
    },
    getEditStyle() {
      return {
        width: this.work.width + "px",
        height: this.work.height + "px",
      };
    },
  },
  created() {
      this.setEditingPage();
      this.initEdit();
  }
};
</script>
<style lang="scss" scoped>
.edit-wrap {
  width: 1000px;
  margin: 0px auto;
}
</style>

<template>
  <div class="props-panel">
    <a-tabs defaultActiveKey="1">
      <a-tab-pane key="1" tab="属性">
        <basic-props :formItems="getEditFormItem()" :formModel="formModel" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
import basicProps from "../basicProps";
import { getVM } from "@editor/utils/element";
import { mapState, mapActions } from "vuex";

export default {
  components: { basicProps },
  props: ["beforeRead"],
  computed: {
    ...mapState("editor", {
      editingElement: (state) => state.editingElement,
    }),
    formModel() {
      return this.editingElement ? this.editingElement.pluginProps : null;
    },
  },
  watch: {
    editingElement: {
      handler(old, nw) {
        if (old && nw && old === nw) {
          this.updateCache();
        }
      },
      deep: true,
    },
  },
  methods: {
    ...mapActions("editor", ["updateCache"]),
    getProps() {
      if (!this.editingElement) {
        return null;
      }
      const vm = getVM(this.editingElement.name);
      const pcProps = vm.$options.pcProps;
      return pcProps;
    },
    getEditFormItem() {
      const pcProps = this.getProps();
      const items = [];
      if (pcProps) {
        Object.entries(pcProps).forEach(([key, config]) => {
          if (config.visible !== false) {
            items.push({
              type: config.editor.type,
              props: Object.assign({}, config.editor.props || {}),
              propsName: key,
              config: config.editor,
            });
          }
          if (config.editor.type == "pc-upload") {
            items[items.length - 1].props.beforeRead = this.beforeRead;
          }
        });
      }

      return items;
    },
  },
};
</script>

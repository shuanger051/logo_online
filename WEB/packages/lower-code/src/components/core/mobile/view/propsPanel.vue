<template>
  <van-popup
    v-model="show"
    :overlay="false"
    round
    position="bottom"
    :style="{ height: '30%' }"
  >
    <basic-props :formItems="getEditFormItem()" :formModel="formModel" />
  </van-popup>
</template>
<script>
import basicProps from "../basicProps";
import { getVM } from "@editor/utils/element";
import { mapState, mapActions } from "vuex";

export default {
  components: { basicProps },
  props: ["breforRead"],
  computed: {
    ...mapState("editor", {
      editingElement: (state) => state.editingElement,
    }),
    formModel() {
      return this.editingElement ? this.editingElement.pluginProps : null;
    },
  },
  data() {
    return {
      show: false,
    };
  },
  watch: {
    editingElement: {
      handler(old, nw) {
        this.show = this.editingElement ? true : false;
        if (old && nw && old === nw) {
          this.updateCache()
        }
      },
     deep: true
    },
  },
  methods: {
    ...mapActions("editor", ["updateCache"]),
    getMobileProps() {
      if (!this.editingElement) {
        return null;
      }
      const vm = getVM(this.editingElement.name);
      const mobileProps = vm.$options.mobileProps;
      return mobileProps;
    },
    getEditFormItem() {
      const mobileProps = this.getMobileProps();
      const items = [];
      if (mobileProps) {
        Object.entries(mobileProps).forEach(([key, config]) => {
          if (config.visible !== false) {
            items.push({
              type: config.editor.type,
              props: Object.assign({}, config.editor.props || {}),
              propsName: key,
              config: config.editor,
            });
          }
          if (config.editor.type == "mobile-upload") {
            items[items.length - 1].props.beforeRead = this.breforRead;
          }
        });
      }
      return items;
    },
  }
};
</script>

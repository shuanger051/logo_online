<template>
  <div class="props-panel">
    <a-tabs defaultActiveKey="1">
     <a-tab-pane key="1" tab="属性">
      <basic-props :formItems = "getEditFormItem()" :formModel = "formModel" />
     </a-tab-pane>

  </a-tabs>
  </div>
</template>
<script>
  import basicProps from '../basicProps';
  import { getVM } from '@editor/utils/element'
  import { mapState } from "vuex";

  export default {
    components: {basicProps},
    computed: {
      ...mapState('editor', {
        editingElement: state => state.editingElement,
      }),
      formModel() {
        return this.editingElement ? this.editingElement.pluginProps : null
      }
    },
    methods: {
      getProps() {
        if (!this.editingElement) {
          return null
        }
        const vm = getVM(this.editingElement.name);
        const pcProps = vm.$options.pcProps;
        return  pcProps
      },
      getEditFormItem() {
        const pcProps = this.getProps();
        const items = []
        if (pcProps) {
          Object.entries(pcProps).forEach(([key, config])=> {
            if (config.visible !== false) {
              items.push({
                type: config.editor.type,
                props: config.editor.props,
                propsName: key,
                config: config.editor
              })
            }
          })
        }
        return items
      }
    }
  }
</script>

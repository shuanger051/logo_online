<template>
  <van-popup v-model="show" :overlay="false"  round position="bottom" :style="{ height: '30%' }">
      <basic-props :formItems = "getEditFormItem()" :formModel = "formModel" />
  </van-popup>
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
    data() {
      return {
        show: false
      }
    },
    watch: {
      editingElement() {
        this.show = this.editingElement ? true : false
      }
    },
    methods: {
      getMobileProps() {
        if (!this.editingElement) {
          return null
        }
        const vm = getVM(this.editingElement.name);
        const mobileProps = vm.$options.mobileProps;
        return  mobileProps
      },
      getEditFormItem() {
        const mobileProps = this.getMobileProps();
        const items = []
        if (mobileProps) {
          Object.entries(mobileProps).forEach(([key, config])=> {
            items.push({
              type: config.editor.type,
              props: config.editor.props,
              propsName: key
            })
          })
        }
        console.log(items)
        return items
      }
    }
  }
</script>
import { getEditRuntimeContext } from '@h5Designer/core/runtime_context'
import { mapActions, mapState, mapGetters } from 'vuex'
export default {
  name: 'ePropsConfig',
  props: ['editorPropsConfig', 'element_name', 'elements'],
  data() {
    return {
    }
  },
  methods: {
    ...mapActions('cms/elements', ['updateElementProperty', 'updateElementStyle']),
    renderPropsEditorPanel (h) {
      return (
        this.componentConfig
      )
    }
  },
  render (h) {
    let runtimeContext = getEditRuntimeContext()
    let config = runtimeContext.configGetter.getConfig(this.element_name)
    let Comp = config.renderEditPanel
    let props = {
      selectedPage: this.selectedPage,
      selectedElement: this.selectedElement,
      selectedElementData: this.selectedElementData,
      elements: this.elements,
      context: {
        updateElementProperty: this.updateElementProperty,
        updateElementStyle: this.updateElementStyle,
        editorPropsConfig: this.editorPropsConfig
      }
    }
    return (
      typeof Comp === 'function' ?
        Comp(h, props) :
        h(Comp, {
          props: props
        })
    )
  },
  computed: {
    ...mapState('cms/editState', [
      'selectedElement',
      'selectedPage'
    ]),
    ...mapGetters('cms/editState', [
      'selectedElementData'
    ])
  }
}

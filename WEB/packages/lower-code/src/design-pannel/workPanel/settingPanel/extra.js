import { getEditRuntimeContext } from '@h5Designer/core/runtime_context'
import {mapActions, mapGetters, mapState} from 'vuex'
export default {
  name: 'extraConfig',
  props: ['index'],
  data() {
    return {
    }
  },
  methods: {
    ...mapActions('cms/elements', ['updateElementProperty', 'updateElementStyle'])
  },
  render (h) {
    let runtimeContext = getEditRuntimeContext()
    let config = runtimeContext.configGetter.getConfig(this.selectedElementData['name'])
    let Comp = config.extraRenderPanel[this.index].template
    let props = {
      selectedPage: this.selectedPage,
      selectedElement: this.selectedElement,
      selectedElementData: this.selectedElementData,
      // elements: this.elements,
      context: {
        updateElementProperty: this.updateElementProperty,
        updateElementStyle: this.updateElementStyle
        // editorPropsConfig: this.editorPropsConfig
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

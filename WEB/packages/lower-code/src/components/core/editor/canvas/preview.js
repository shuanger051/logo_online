/**
 * 预览模块
 * preview h5 work module
 */

import NodeWrapper from 'core/preview/node-wrapper.js'

export default {
  props: ['elements', 'height'],
  components: {
    NodeWrapper
  },
  methods: {
    renderPreview () {
      const elements = this.elements || []
      const pageWrapperStyle = { height: this.height || '100%', position: 'relative' }
      return (
        <div style={pageWrapperStyle}>
          {
            elements.map((element, index) => {
              return <node-wrapper element={element}>
                {
                  this.$createElement(element.uuid, {
                    ...element.getPreviewData({ isNodeWrapper: false }),
                  })
                }
              </node-wrapper>
            })
          }
        </div>
      )
    }
  },
  render (h) {
    return this.renderPreview()
  }
}

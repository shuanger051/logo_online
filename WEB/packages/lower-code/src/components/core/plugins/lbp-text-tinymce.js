// https://github.com/luban-h5-components/plugin-common-props
import PropTypes from '@luban-h5/plugin-common-props'
import font from 'core/styles/fontMap'

export default {
  render (h) {
    const style = {
      fontFamily: this.fontFamily || 'arial',
      position: 'relative',
      color: `${this.fontColor} !important`,
      textDecoration: 'none',
      backgroundColor: this.backgroundColor || 'unset',
      lineHeight: `${this.lineHeight}em`,
      border: `${this.borderWidth}px solid ${this.borderColor}`,
      borderRadius: `${this.borderRadius}px`,
      fontSize: `${this.fontSize}px` || '14px'
    }
    return <div style={style} domPropsInnerHTML={this.text} class="ql-editor ql-container"></div>
  },
  name: 'lbp-text-tinymce',
  data () {
    return {
      innerText: this.text || '双击修改文字'
    }
  },
  props: {
    fontColor: PropTypes.color({ label: '颜色', defaultValue: '#000' }),
    fontFamily: PropTypes.select({ defaultValue: '', label: '字体', options: font }),
    fontSize: PropTypes.number({ defaultValue: 14, label: '字体大小(px)', options: font }),
    borderWidth: PropTypes.number({ label: '边框宽度(px)', defaultValue: 0 }),
    borderRadius: PropTypes.number({ label: '圆角(px)' }),
    borderColor: PropTypes.color({ label: '边框颜色' }),
    text: PropTypes.string({ label: '内容',
      defaultValue: '双击修改文字',
      visible: true,
      component: 'tinymce-editor',
      props: {
        init: {
          menubar: false,
          toolbar:
            `undo redo | formatselect | bold italic backcolor | \
            alignleft aligncenter alignright alignjustify | \
            bullist numlist outdent indent | removeformat | help`
        }
      }
    }),
    editorMode: PropTypes.string({
      defaultValue: 'preview', // 可选值: preview/edit
      label: '模式',
      visible: false
    }),
    isEditingElement: PropTypes.boolean({
      defaultValue: false, // 可选值: preview/edit
      label: '是否当前元素',
      visible: false
    })
  },
  editorConfig: {
  }
}

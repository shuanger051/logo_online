// import config from './e-text.js'
import config from './e-text.vue'
export default {
  elementRemark: '文本',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 50,
    top: 0,
    left: 0,
    'border-color': '#000',
    'border-width': 0,
    'border-top-style': 'solid',
    'border-bottom-style': 'solid',
    'border-left-style': 'solid',
    'border-right-style': 'solid',
    'border-radius': 0,
    'overflow': 'hidden',
    'display': 'flex'
  },
  // 默认属性
  defaultProperty: {
    'content': '双击编辑文本',
    'font-size': 14,
    'letter-spacing': 1,
    'line-height': 1.5,
    'font-weight': 'normal',
    'font-style': 'normal',
    'text-decoration': 'none',
    'text-align': 'left',
    'padding-left': 10,
    'padding-right': 10,
    'padding-top': 10,
    'padding-bottom': 10,
    'color': '#000',
    'word-wrap': 'break-word',
    'word-break': 'break-all',
    'overflow': 'hidden',
    'display': 'flex',
    'justify-content': 'flex-start',
    'align-items': 'flex-start'
  },
  // 默认能力
  editDefaultAbility: {
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true
  },
  renderEditPanel: config
}

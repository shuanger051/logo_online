import config from './e-button.vue'
export default {
  elementRemark: '按钮',
  // 默认样式
  defaultStyle: {
    width: 100,
    height: 38,
    top: 0,
    left: 0,
    'border-color': '#000',
    'border-width': 0,
    'border-top-style': 'solid',
    'border-bottom-style': 'solid',
    'border-left-style': 'solid',
    'border-right-style': 'solid',
    'border-radius': 0,
    'overflow': 'auto'
  },
  // 默认属性
  defaultProperty: {
    version: '1.6.1',
    'width': '100%',
    'height': '100%',
    'content': '按钮',
    'font-size': 12,
    'letter-spacing': 0,
    'line-height': 1,
    'font-weight': 'normal',
    'font-style': 'none',
    'text-decoration': 'none',
    'text-align': 'center',
    'display': 'flex',
    'align-items': 'center',
    'justify-content': 'center',
    'padding-left': 0,
    'padding-right': 0,
    'padding-top': 0,
    'padding-bottom': 0,
    'color': '#000',
    /* ----- */
    'background': '',
    'background-color': '#59c7f9',
    /* ----- */
    'background-image': '',
    /* ----- */
    'button-type': 'normal',
    'overflow': 'auto'
  },
  // 公共能力显示隐藏
  editPanelConfig: {
    backgroundColorSet: {
      hide: true
    }
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

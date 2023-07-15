import Image from './e-image'

export default {
  elementRemark: '图像',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 185,
    top: 0,
    left: 0,
    'border-color': '#000',
    'border-width': 0,
    'border-top-style': 'solid',
    'border-bottom-style': 'solid',
    'border-left-style': 'solid',
    'border-right-style': 'solid',
    'border-radius': 0,
    'overflow': 'hidden'
  },
  // 默认属性
  defaultProperty: {
    width: 375,
    height: 185,
    top: 0,
    left: 0,
    src: ''
  },
  // 外部mix方法
  mixinMethods: ['clickFn'],
  // 对外发布的事件
  publicEvents: [],
  // 订阅的外部事件
  subscribeEvents: [],
  // 默认能力
  editDefaultAbility: {
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true
  },
  // 渲染编辑面板
  renderEditPanel: Image,
  // 编辑面板配置
  editPanelConfig: {
    // hideEvents: true,
    backgroundColorSet: {
      hide: true
    },
    positionSet: {
      hide: false,
      left: {
        disabled: false
      }
    },
    sizeSet: {
      hide: false,
      width: {
        disabled: false
      }
    }
  }
}

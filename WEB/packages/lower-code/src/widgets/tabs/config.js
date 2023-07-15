// import config from './e-tabs.js'
import config from './e-tabs'

export default {
  elementRemark: '标签页',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 200,
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
    type: 'line',
    activeIndex: 0,
    activeTime: 0,
    'tab-customer-fontSize': 12,
    'tab-customer-selected-fontSize': 14,
    'tab-customer-fontWeight': 'normal',
    'tab-customer-selected-fontWeight': 'bold',
    'tab-customer-style': 'none',
    'tab-customer-selected-style': 'none',
    'text-customer-direction': 'none',
    'text-customer-selected-direction': 'none',
    'tab-customer-color': '#000',
    'tab-selected-color': '#000',
    'background-color': '#fff',
    'selected-background-color': '#fff',
    tabList: [{
      title: '标签1',
      id: '1', // generateUID()
      elementContent: [],
      canUseElement: []
    }, {
      title: '标签2',
      id: '2',
      elementContent: [],
      canUseElement: []
    }, {
      title: '标签3',
      id: '3',
      elementContent: [],
      canUseElement: []
    }]
  },
  // 默认能力
  editDefaultAbility: {
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true
  },
  renderEditPanel: config,
  // 编辑面板配置
  editPanelConfig: {
    hideEvents: true,
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

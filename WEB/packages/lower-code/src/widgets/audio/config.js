import config from './e-audio.vue'
import { generateUID } from '@h5Designer/utils'

export default {
  // 默认样式
  defaultStyle: {
    width: 30,
    height: 30,
    top: 0,
    left: 0,
    'border-color': '#000',
    'border-width': 0,
    'border-top-style': 'solid',
    'border-bottom-style': 'solid',
    'border-left-style': 'solid',
    'border-right-style': 'solid',
    'border-radius': '15px'
  },
  elementRemark: '音频',
  // 默认属性
  defaultProperty: {
    'loop': false,
    'audioSrc': null,
    'audioName': '',
    'background': 'rgba(255,255,255,1)',
    'border-radius': '15px',
    uuid: generateUID()
  },
  // 默认能力
  editDefaultAbility: {
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true
  },
  // 编辑面板配置
  editPanelConfig: {
    // hideEvents: true,
    backgroundColorSet: {
      hide: true
    }
  },
  renderEditPanel: config
}

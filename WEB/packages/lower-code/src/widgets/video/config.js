import config from './e-video.vue'
import { generateUID } from '@h5Designer/utils'

export default {
  elementRemark: '视频',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 198,
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
    videoSourceType: '1',
    'loop': false,
    'videoSrc': null,
    videoOutSrc: '',
    'videoName': '',
    'poster': '',
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

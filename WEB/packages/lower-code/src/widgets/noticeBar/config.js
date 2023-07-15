import NoticeBar from './e-noticeBar'

export default {
  'elementRemark': '跑马灯',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 50,
    top: 0,
    left: 0,
    'overflow': 'hidden'
  },
  // 默认属性
  defaultProperty: {
    speed: 2, // 字幕滚动速度
    messageList: [
      {
        'order_no': 1,
        'op_desc': '滚动跑马灯消息1'
      },
      {
        'order_no': 2,
        'op_desc': '滚动跑马灯消息2'
      },
      {
        'order_no': 3,
        'op_desc': '滚动跑马灯消息3'
      }
    ],
    'font-size': 14,
    'position': 'relative',
    'font-weight': 'normal',
    'font-style': 'none',
    'text-decoration': 'none',
    'background-color': '#fff7cc',
    'color': '#f60',
    'overflow': 'hidden',
    'white-space': 'nowrap',
    'display': 'flex',
    'align-items': 'center'
  },
  // 默认能力
  editDefaultAbility: {
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true,
    scalePoint: [
    ],
    direction: 'vertical'
  },
  // 渲染编辑面板
  renderEditPanel: NoticeBar,
  // 公共能力显示隐藏
  editPanelConfig: {
    hideEvents: true,
    borderSet: {
      hide: true
    },
    positionSet: {
      hide: true,
      left: {
        disabled: true
      }
    },
    sizeSet: {
      hide: true
    },
    backgroundColorSet: {
      hide: true
    }
  }
}

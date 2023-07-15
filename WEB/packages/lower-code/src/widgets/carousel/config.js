import Carousel from './e-carousel'

export default {
  'elementRemark': '轮播图',
  // 默认样式
  defaultStyle: {
    width: 375,
    height: 187,
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
    auto_play: '1', // 1自动 2手动 false手动切换
    switch_time: 5, // 自动切换事件
    scale_rate: 1, // 比例
    imgList: [
      {
        src: '',
        action_type: 'none',
        out_url: '',
        android_download_url: '',
        android_jump_url: '',
        ios_jump_url: '',
        ios_download_url: ''
      }
    ],
    activeIndex: 1// 选中哪一个图片
  },
  // 默认能力
  editDefaultAbility: {
    // scalePoint: [
    //   {
    //     key: 't'
    //   },
    //   {
    //     key: 'b'
    //   }
    // ],
    // direction: 'vertical',
    // 是否能横行拖动
    canXResize: true,
    // 是否能纵轴拖动
    canYResize: true
  },
  renderEditPanel: Carousel,
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

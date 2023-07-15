
const optaionBtns = [
  {
    i18nTooltip: '撤销一步(ctrl+z)',
    icon: 'reply',
    disabled: 'undo',
    action: function () { this.undoOrRedo(true) },
    hotkey: 'ctrl&z,⌘&z',
    hotkeyTooltip: '(ctrl+z)'
  },
  {
    i18nTooltip: '还原一步(ctrl+y)',
    icon: 'forward',
    disabled: 'redo',
    action: function () { this.undoOrRedo(false) },
    hotkey: 'ctrl&y,⌘&y',
    hotkeyTooltip: '(ctrl+y)'
  }
  // {
  //   i18nTooltip: '前置一层(alt++)',
  //   icon: 'social-buffer',
  //   disabled: 'pre',
  //   action: function () { this.adjustElement('pre') },
  //   hotkey: 'alt&+,⌘&+',
  //   hotkeyTooltip: '(alt++)'
  // },
  // {
  //   i18nTooltip: '后置一层(alt+-)',
  //   icon: 'social-buffer-outline',
  //   disabled: 'post',
  //   action: function () { this.adjustElement('post') },
  //   hotkey: 'alt&-,⌘&-',
  //   hotkeyTooltip: '(alt+-)'
  // },
  // {
  //   i18nTooltip: '放大(ctrl+up)',
  //   icon: 'plus',
  //   unShow: true,
  //   action: function () { this.handleZoomOn() },
  //   hotkey: 'ctrl&up,⌘&up',
  //   hotkeyTooltip: '(ctrl+up)'
  // },
  // {
  //   i18nTooltip: '缩小(ctrl+down)',
  //   icon: 'minus',
  //   unShow: true,
  //   action: function () { this.handleZoomIn() },
  //   hotkey: 'ctrl&down,⌘&down',
  //   hotkeyTooltip: '(ctrl+down)'
  // },
  // {
  //   i18nTooltip: '预览',
  //   icon: 'view',
  //   disabled: 'default',
  //   action: function () { this.previewWork() }
  // },
  // {
  //   i18nTooltip: '生成长图',
  //   icon: 'android-camera',
  //   disabled: 'default',
  //   action: function () { this.exportLongImg() }
  // }
]

export default optaionBtns

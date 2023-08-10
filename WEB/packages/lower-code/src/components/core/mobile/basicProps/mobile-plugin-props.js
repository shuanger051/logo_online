const defaultNumberInputProp = {
  // step: 1,
  // min: 0,
  // max: 144
  "input-width": "50px",
  disabled: true
}



export default {
  boolean: ({ label = '开关', defaultValue = false, props = {}, visible = true, ...other } = {}) => ({
    type: Boolean,
    default: defaultValue,
    /**
     *
     * !#zh: 是否在属性配置面板显示
     * !#en: whether visible in props config panel
     *
     * 正常情况下都是显示在右侧属性面板中，支持用户配置的
     *
     * 但有些属性是不希望 暴露给用户进行配置的，
     * 比如：editorMode(edit/preview) 当前编辑器模式(编辑模式/预览模式)，
     * 这些是系统注入到组件中，不希望用户进行配置的
     *
     */
    visible,
    editor: {
      type: 'van-switch',
      label,
      props,
      ...other
    }
  }),
  required: {
    type: Boolean,
    default: false
  },
  vertical: {
    type: Boolean,
    default: false
  },
  /**
   * 颜色 默认编辑器
   * Q: 为什么 transparent 无效？
   * A: 注意，根据 MDN 文档，颜色选择器的 value 只能是：# + 6个16进制字符串
   * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color#Value
   * The value of an <input> element of type color is always a DOMString which contains a 7-character string specifying an RGB color in hexadecimal format.
   *
   */
  color: ({ label = '文字颜色', defaultValue = '#000000', visible = true, ...other } = {}) => ({
    type: String,
    // 注意，根据 MDN 文档，颜色选择器的 value 只能是：# + 6个16进制字符串
    // https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color#Value
    // The value of an <input> element of type color is always a DOMString which contains a 7-character string specifying an RGB color in hexadecimal format.
    default: defaultValue,
    visible,
    editor: {
      type: 'el-color-picker',
      label,
      // !#zh 为编辑组件指定 props
      props: {
        // size: 'mini',
        showAlpha: true
      },
      require: true,
      ...other
    }
  }),
  
  /**
   * 数值类型 默认编辑器
   */
  number: ({ label = '数值', defaultValue = 10, props = defaultNumberInputProp, visible = true, ...other } = {}) => ({
    type: Number,
    default: defaultValue,
    visible,
    editor: {
      type: 'van-stepper',
      label,
      require: true,
      props,
      ...other
    }
  }),

  string: ({ label = '按钮文字', defaultValue = '按钮', component = 'van-field', props = {}, extra, visible = true, ...other } = {}) => ({
    type: String,
    default: defaultValue,
    visible,
    editor: {
      type: component,
      label,
      require: true,
      props,
      extra,
      ...other
    }
  }),

  image: ({ label = '图片', defaultValue = '', visible = true, ...other } = {}) => ({
    type: String,
    default: defaultValue,
    visible,
    editor: {
      type: 'lbs-image-gallery',
      label,
      ...other
    }
  }),

  select: ({ valueType = String, label = '选项', defaultValue = [], visible = true, options = [], ...other } = {}) => ({
    type: valueType,
    default: defaultValue,
    visible,
    editor: {
      type: 'mobile-select',
      label,
      props: {
        options,
        label
      },
      ...other
    }
  })
}

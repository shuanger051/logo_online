import Vue from 'vue'

import LbpText from 'core/plugins/lbp-text'
import LbpTextTinymce from 'core/plugins/lbp-text-tinymce'
import LbpPicture from 'core/plugins/lbp-picture'


export const pluginsList = [

  {
    title: '图片',
    i18nTitle: {
      'en-US': 'Picture',
      'zh-CN': '图片'
    },
    icon: 'photo',
    component: LbpPicture,
    visible: true,
    name: LbpPicture.name
  },
  {
    i18nTitle: {
      'en-US': 'Text',
      'zh-CN': '文字'
    },
    title: '文字',
    icon: 'text-width',
    component: LbpText,
    visible: true,
    name: LbpText.name
  },
  {
    i18nTitle: {
      'en-US': 'Tiny-Text',
      'zh-CN': 'Tiny-文字'
    },
    title: 'Tiny-文字',
    icon: 'text-width',
    component: LbpTextTinymce,
    visible: true,
    name: LbpTextTinymce.name
  }
]

export default {
  data: () => ({
    pluginsList
  }),
  methods: {
    mixinPlugins2Editor () {
      pluginsList.forEach(plugin => {
        // 全局注册组件，便于以后扩展自定义脚本，注释原来的局部注册：this.$options.components[plugin.name] = plugin.component
        Vue.component(plugin.name, plugin.component)
      })
    }
  },
  created () {
    this.mixinPlugins2Editor()
  }
}

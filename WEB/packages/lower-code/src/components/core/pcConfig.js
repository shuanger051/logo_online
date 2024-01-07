import Vue from 'vue'
import AsyncImage from 'core/plugins/AsyncImage'

import components from 'core/pc/component'
import plugins from "core/plugins/index"
import createConfig from './Config'
plugins.methods.mixinPlugins2Editor()
Vue.component(AsyncImage.name, AsyncImage)

Object.entries(components).forEach(([key, component]) => {
  Vue.component(key, component)
})

export default createConfig(() => {
  return {
  }
})
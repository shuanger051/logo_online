import Vue from 'vue'

import components from 'core/mobile/component'
import plugins from "core/plugins/index"
import createConfig from './Config'
plugins.methods.mixinPlugins2Editor()

Object.entries(components).forEach(([key, component]) => {
  Vue.component(key, component)
})

export default createConfig(() => {
  return {
    instanceHandler: {
      
    },
    installHandler: {
      
    }
  }
})
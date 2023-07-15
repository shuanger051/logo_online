/**
 * 组件的配置对象，
 * 提供组件的配置信息查找等
 */

export default class ConfigGetter {
  constructor() {
    this.__store = new Map()
  }

  add(configs) {
    for (var i = 0; i < configs.length; i++) {
      let item = configs[i]
      this.__store.set(item.name, item.config)
    }
  }

  getName(name) {
    let config = this.getConfig(name)
    return config.name
  }

  getPropsConfig(name) {
    let config = this.getConfig(name)
    return config.PropsConfig
  }

  getCommonConfig(name) {
    let config = this.getConfig(name)
    return config.CommonConfig
  }

  getConfig(name) {
    return this.__store.get(name)
  }

  format() {}
}

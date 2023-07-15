import trackPlugins from './trackPlugin'

/**
 * plugins 缓存对象
 */
const plugins = {
  trackPlugins
}

/**
 * 增加外置插件
 * @param {*} key
 * @param {*} value
 */
const addPlugins = (key, value) => {
  plugins[key] = value
}

/**
 * 获取当前插件key
 * @returns
 */
const getPlugins = (key) => {
  if (key) {
    return plugins[key]
  } else {
    return plugins
  }
}

/**
 * 删除插件
 */
const deletePlugins = (key) => {
  delete plugins[key]
}

export {
  addPlugins,
  getPlugins,
  deletePlugins
}

import RuntimeContext from './RuntimeContext'
import Edit from './Edit'
import Preview from './Preview'
import { getPlugins } from './runtime_plugins/index'
let store = new Map()
/**
 * 建造者构造器
 * @param {*} ModeCtr
 * @param {*} key
 * @returns
 */
const createRuntimeBuilder = (ModeCtr, key) => {
  const maker = () => {
    // 初始化runtimeContext
    let context = new RuntimeContext()
    let mode = new ModeCtr(context)
    let plugins = Object.values(getPlugins())
    let { eventController } = context
    // mixin当前mode对象
    context.setMode(mode)
    // 添加运行时插件
    context.addPlugins(plugins)
    let { systemSign } = eventController
    // 添加销毁事件
    eventController
      .on(systemSign.runtime.destroyed, () => {
        store.delete(key)
      })

    store.set(key, context)
    return context
  }

  return () => {
    if (store.has(key)) {
      return store.get(key)
    } else {
      return maker()
    }
  }
}
/**
 * 编辑环境运行时构造者
 */
const getEditRuntimeContext = createRuntimeBuilder(Edit, 'edit')
/**
 * 预览环境运行时构造者
 */
const getPreviewRuntimeContext = createRuntimeBuilder(Preview, 'prview')
/**
 * 自定义运行时构造者
 */
const getSpecialRuntimeContext = (mode, key) => {
  if (mode !== 'preview') {
    mode = Preview
  } else {
    mode = Edit
  }
  let builder = createRuntimeBuilder(mode, key)
  return builder()
}

export {
  getEditRuntimeContext,
  getPreviewRuntimeContext,
  getSpecialRuntimeContext
}

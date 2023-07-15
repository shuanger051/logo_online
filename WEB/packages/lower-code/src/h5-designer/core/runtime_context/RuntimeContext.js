import resourceManager from '../resource_manager'
import ComponentsManager from '../comonents_manager'
import EventEmitter from '../events'
import widget from '../../../widgets'
/**
 * RuntimeContext 运行环境
 */
class RuntimeContext {
  constructor() {
    this.__eventController = new EventEmitter()
    this.__componentsManager = new ComponentsManager()
    this.setMode = (mode) => {
      if (this.__mode) {
        console.warn('mode already set')
        return false
      }
      this.__mode = mode
      return true
    }
  }

  initComponents(resourceUrl = []) {
    let comps = null
    // let { systemSign } = this.eventController

    const getInnerWidgets = () => {
        return Promise.all(widget).then((response) => {
          console.log(3333)
          return response.reduce((a, b) => a.concat(b.default), [])
        }).catch((e) => {console(e.message,1111); throw e})
    }

    return (
      Promise
        .all([
          getInnerWidgets(),
          resourceManager.install(resourceUrl)
        ])
        .then((response) => {
          console.log('组件引入')
          console.log(response)
          comps = response[0].concat(response[1].getResource())
          this.__componentsManager.add(comps)
          // this.eventController.emit(systemSign.runtime.inited)
        }).catch((err) => {
          console.log('PromiseAll-err')
          console.log(err)
        })
    )
  }

  addPlugins (plugins) {
    plugins.forEach((item) => {
      item.install(this.mode.getCurrentModeApi())
    })
  }

  /**
   * 获取组件
   * @param  {...any} args d
   * @returns
   */
  getComponent (...args) {
    return this.__componentsManager.getComponent(...args)
  }

  /**
   * 获取组件配置对象
   */
  get configGetter () {
    return this.__componentsManager.configGetter
  }

  /**
   * 获取事件
   */
  get eventController() {
    return this.__eventController
  }

  /**
   * mode
   */
  get mode() {
    return this.__mode
  }

  destroy() {
    let { systemSign } = this.eventController
    this.eventController.emit(
      systemSign.runtime.beforeDestroy
    )
    this.__componentsManager.destroy()
    if (this.mode) {
      this.mode.destroy()
    }
    this.eventController.emit(
      systemSign.runtime.destroyed
    )
    this.eventController.off()
  }
}

export default RuntimeContext

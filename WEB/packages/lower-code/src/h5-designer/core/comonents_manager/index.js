import Pools from './Pools'
import ConfigGetter from './ConfigGetter'
import Creator from './Creator'

/**
 * 格式化资源
 * @param {*} resources
 * @returns
 */
const formart = (resources) => {
  if (!Array.isArray(resources)) {
    resources = [resources]
  }
  let configs = []
  let components = []

  resources.forEach(item => {
    configs.push({
      name: item.name,
      config: item.config
    })
    components.push({
      name: item.name,
      component: item.component
    })
  })
  return {
    configs,
    components
  }
}

/**
 * 组件管理器
 * 创建组件的构造器
 */
export default class Manager {
  constructor() {
    this.__pools = new Pools()
    this.__configGetter = new ConfigGetter()
  }

  /**
   * 增加资源
   * @param {*} resources
   */
  add(resources) {
    if (!this.__creator) {
      this.__creator = new Creator()
    }
    let {
      components,
      configs
    } = formart(resources)

    this.__creator.init(components)
    this.__configGetter.add(configs)
  }
  /**
   * 获取通过uuid和组件名的组件构造器
   * @param {*} uuid
   * @param {*} name
   * @param {*} extend
   * @returns
   */
  getComponent = (uuid, name, objOrFn = []) => {
    let pools = this.__pools
    let ctr = pools.get(uuid)

    const mixIn = {
      props: ['context'],
      methods: {
        __getuuid() {
          return uuid
        }
      },
      beforeDestroy() {
        pools.delete(uuid)
      }
    }

    if (!ctr) {
      let extend = objOrFn
      if (typeof objOrFn === 'function') {
        extend = objOrFn()
      }
      ctr = this.__creator.create(name, [...extend, mixIn])
      pools.update(uuid, ctr)
    }
    return ctr
  }
  destroy () {
    if (this.__creator) {
      this.__creator.destory()
    }
    this.__pools.clear()
  }
  /**
   * 获取组件配置文件
   */
  get configGetter () {
    return this.__configGetter
  }
}

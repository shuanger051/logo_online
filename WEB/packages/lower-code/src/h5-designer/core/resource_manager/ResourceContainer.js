import {cloneDeep} from 'lodash'

/**
 * 资源容器
 * 三方组件和内置组件的资源容器
 */
class ResourceContainer {
  constructor() {
    this.accumulator = new Map()
  }

  addResources(resources) {
    let len = resources.length
    let item

    for (var i = 0; i < len; i++) {
      item = resources[i]
      if (!item.name) {
        console.error('resource need name')
      } else {
        if (this.accumulator.has(item.name)) {
          console.error(`resource: ${item.name} is exist`)
        } else {
          this.accumulator.set(item.name, item)
        }
      }
    }
  }

  getResource(names) {
    let cloneResource = []
    if (!names) {
      names = this.getResourceKeys()
    } else if (!Array.isArray(names)) {
      names = [names]
    }

    this.accumulator.forEach((item) => {
      cloneResource.push(cloneDeep(item))
    })
    return cloneResource
  }

  getResourceKeys() {
    return this.accumulator.keys()
  }
}

export default new ResourceContainer()

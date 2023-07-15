import Vue from 'vue'

const creatorHandlerMaker = (ctr) => {
  let base = Vue.extend(ctr)

  return (mixins) => {
    let ctr = base
    if (!Array.isArray(mixins)) {
      mixins = [mixins]
    }
    mixins.forEach(item => {
      ctr = ctr.extend(item)
    })
    return ctr
  }
}
/**
 * 组件构造者
 */
export default class Creator {
  init (ctrs) {
    this.__store = this.__store || new Map()
    let len = ctrs.length

    for (let i = 0; i < len; i++) {
      let item = ctrs[i]
      let name = item.name
      let ctr = item.component

      if (this.__store.has(name)) {
        // console.info('component constructor:' + name + ',is exist')
      } else {
        this.__store.set(
          name,
          creatorHandlerMaker(ctr)
        )
      }
    }
  }

  create(name, mixins) {
    let ctrMaker = this.__store.get(name)
    return ctrMaker(mixins)
  }

  destory() {
    this.__store = null
  }
}

/**
 * 组件容器池子
 */
export default class Pools {
  constructor() {
    this.pools = new Map()
    this.callbacks = new Map()
  }
  /**
   * 池子更新
   * @param {*} name
   * @param {*} matter
   * @param {*} callback
   * @param {*} force
   * @returns
   */
  update(name, matter, callback, force) {
    if (
      !this.pools.has(name)
    ) {
      this.pools.set(name, matter)

      if (callback) {
        this.callbacks.set(name, callback)
      }
      return matter
    }

    if (force) {
      this.delete(name)
    }
    this.update(name, matter, callback)
  }

  delete(name) {
    let matter = this.pools.get(name)
    let fn = this.callbacks.get(name)
    if (fn) {
      fn(name, matter)
    }
    this.pools.delete(name)
    this.callbacks.delete(name)
  }

  get(name) {
    let matter = this.pools.get(name)
    return matter
  }

  clear() {
    let names = this.pools.keys()
    for (var i = 0; i < names.length; i++) {
      this.delete(names[i])
    }
    this.pools = new Map()
    this.callbacks = new Map()
  }
}

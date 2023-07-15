/**
 * 依赖收集器
 */
export class Dep extends Set {
  constructor(key) {
    super()
    this.__key = key
  }
  flush() {
    for (let fn of this.values()) {
      try {
        fn()
      } catch (e) {
        console.warn(`[${this.__key}: flushDep]: `, e)
      }
    }
  }
}

/**
 * 基于模板的action 基类
 */
import {lockfn, Dep} from '@h5Designer/utils'

export default class BaseAction {
  constructor(options, ctx) {
    // action依赖对象
    this.dep = new Dep('action')
    this.options = options
    this.ctx = ctx
  }

  /**
   * 初始化action
   */
  init() {
    if (this.bind) {
      this.bind()
    }

    this.play =
      lockfn((done) => {
        if (this.exec) {
          this.exec(done)
        }
      })

    if (this.afterbind) {
      this.afterbind()
    }
  }

  destroy() {
    this.dep.flush()
    if (this.unbind) {
      this.unbind()
    }
  }

  get params() {
    return this.options.params
  }

  get eventController () {
    return this.ctx.eventController
  }
}

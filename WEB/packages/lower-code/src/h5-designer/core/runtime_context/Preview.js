import systemSign from '../events/systemSign'
import {nativeEventAdder} from '@h5Designer/utils'
import resolveEvents from '../eventResolve'
const { created, beforeDestroy, destroyed, click } = systemSign.component
/**
 * 预览环境
 */

const createLifeHanler = (life, uuid) => {
  return function () {
    let eventController =
      this.context && this.context.eventController

    if (eventController) {
      eventController.emit(life, this)
      eventController.emit(uuid + '_' + life, this)
    }
  }
}

export default class Preview {
  constructor(runtime) {
    this.runtime = runtime
  }

  /**
   * 解析event
   * @param {*} eventsData
   */
  resolveEvents(eventsData) {
    this.eventApi = resolveEvents(eventsData, this.runtime)
  }
  /**
   * 组件扩展
   */
  resolveComponentMixIn(uuid) {
    return () => {
      let mixIn = []
      // 生命周期MixIn
      let lifeMixIn = {
        created: createLifeHanler(created, uuid),
        beforeDestroy: createLifeHanler(beforeDestroy, uuid),
        destroyed: createLifeHanler(destroyed, uuid)
      }

      // event mixin
      // TODO: 是否需求过滤事件
      let eventAdder = nativeEventAdder()
      let eventMixIn = {
        mounted() {
          ['click'].forEach((name) => {
            eventAdder.add(this.$el, name, () => {
              let {eventController} = this.context
              eventController.emit(uuid + '_' + name)
              eventController.emit(click, uuid)
            })
          })
        },
        beforeDestroy() {
          eventAdder.off()
        }
      }

      mixIn.push(lifeMixIn, eventMixIn)
      return mixIn
    }
  }

  /**
   * 预览模式下提供二开的api
   * @returns
   */
  getCurrentModeApi() {
    return {
      mode: 'preview',
      eventController: this.runtime.eventController
    }
  }
  destroy() {
    // this.eventApi.destroy()
  }
}

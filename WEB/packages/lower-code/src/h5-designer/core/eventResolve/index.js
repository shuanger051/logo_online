import middlewareArray from './middleware'
import createAction from '../actions/actionCreator'
import EventBus from '@h5Designer/utils/eventBus'
import { withlock, Dep, eachSeries } from '@h5Designer/utils'
/**
 * 解析event 数据
 * @param {*} events
 * @param {*} context
 */
const resolveEvents = (events, context) => {
  let i = 0, len = events.length
  let { eventController } = context
  let accumulator = new Dep()
  withlock(accumulator)

  for (; i < len; i++) {
    let { trigger, params } = events[i]
    // 创建action
    let action = createAction(events[i], accumulator, context)
    action.init()
    // 创建事件中间件
    let middleware = middlewareArray.map((fn) => fn({ ...params }))
    let triggerHandler = () => {
      eachSeries(middleware, (fn, i, next) => {
        fn(next)
      })
    }
    middleware.push(() => {
      action.play()
    })
    let triggerTag = trigger.target + '_' + trigger.value
    console.log('trigger.target + _ + trigger.value', trigger.target + '_' + trigger.value)
    let destroyTag = trigger.target + '_destroyed'
    let destroyHandler = () => {
      eventController.off(destroyTag, handler)
      action.destroy()
    }
    if (trigger.value === 'afterClick') {
      EventBus.$on("hasSubmit", (msg) => {
        if (msg) {
          triggerHandler()
        }
      })
    } else {
      eventController.on(triggerTag, triggerHandler)
      eventController.once(destroyTag, destroyHandler)
      accumulator.add(destroyHandler)
    }

  }

  return createReturnApi(accumulator)
}

/**
 * action工厂对应的返回api接口
 */
const createReturnApi = (accumulator) => {
  return {
    // 销毁actions
    destroy() {
      // 销毁存储器时锁定
      // 防止多次重复删除
      accumulator.dolocked(() => {
        accumulator.flush()
        accumulator.clear()
      })
    }
  }
}

export default resolveEvents

import {eachSeries} from './controller'
import {nativeEventAdder} from '@h5Designer/utils'
export const animation = (el, animationArray, callback) => {
  let nextStep
  let eventAdder = nativeEventAdder()
  /**
   * 动画结束时处理
   */
  let endHandler = () => {
    eventAdder.off()
    callback()
  }
  /**
   * 单个动画结束时处理
   */
  let nextHandler = () => {
    if (nextStep) {
      nextStep()
    }
  }

  eventAdder.add(el, 'animationend', nextHandler)
  eventAdder.add(el, 'animationcancel', endHandler)

  eachSeries(animationArray, (animation, i, next) => {
    el.style.animationName = animation.type
    el.style.animationDuration = `${animation.duration || 0.2}s`
    el.style.animationIterationCount = animation.interationCount || 1
    el.style.animationDelay = `${animation.delay || 0}s`
    el.style.animationFillMode = 'both'
    nextStep = next
  }, endHandler)
}

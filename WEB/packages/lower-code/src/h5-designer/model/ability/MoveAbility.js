/**
 * 元素的能力：移动
 */

import BaseAbility from './BaseAbility'
import { direction } from './constant'
import {nativeEventAdder} from '@h5Designer/utils'
const noop = (arg) => arg
class MoveAbility extends BaseAbility {
  constructor(options) {
    super(options)
    // 初始化默认配置
    this.setOptions({
      // 各个方向是否可移动，
      // 1: 可移动， 0: 不可
      moveHandler: noop,
      // hook: 停止移动触发
      moveUpHandler: noop,
      ...options
    })

    this.setDirection(options.direction)
  }

  start(position) {
    let {
      direction,
      moveHandler,
      moveUpHandler
    } = this.options
    let {clientX: x, clientY: y} = position
    let eventAdder = nativeEventAdder()
    // 绑定鼠标移动事件
    eventAdder.add(document, 'mousemove', (event) => {
      let pos = {
        dx: 0,
        dy: 0
      }
      let {clientX, clientY} = event
      let dx = clientX - x
      let dy = clientY - y

      if (
        (dx > 0 && direction.r) ||
        (dx < 0 && direction.l)
      ) {
        pos.dx = dx
      }

      if (
        (dy > 0 && direction.b) ||
        (dy < 0 && direction.t)
      ) {
        pos.dy = dy
      }
      moveHandler(pos, event)
      event.stopPropagation()
      event.preventDefault()
    })

    // 绑定鼠标松开事件
    eventAdder.add(document, 'mouseup', (event) => {
      let pos = {
        dx: 0,
        dy: 0
      }
      let {clientX, clientY} = event
      let dx = clientX - x
      let dy = clientY - y

      if (
        (dx > 0 && direction.r) ||
        (dx < 0 && direction.l)
      ) {
        pos.dx = dx
      }

      if (
        (dy > 0 && direction.b) ||
        (dy < 0 && direction.t)
      ) {
        pos.dy = dy
      }
      moveUpHandler(pos, event)
      eventAdder.off()
      event.stopPropagation()
      event.preventDefault()
    })
  }

  setDirection (strOrObject) {
    if (!strOrObject || typeof strOrObject === 'string') {
      let value = direction[strOrObject] || direction['all']
      this.setOptions({
        direction: {...value}
      })
    } else {
      this.setOptions(strOrObject)
    }
  }

  destroy() {
    return true
  }
}

export default MoveAbility

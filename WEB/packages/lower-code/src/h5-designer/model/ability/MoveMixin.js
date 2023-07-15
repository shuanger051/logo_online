import MoveAbility from './MoveAbility'
import {addNativeEvent} from '@h5Designer/utils'

const emitHandler = (eventName) => {
  return function (...args) {
    return this.$emit(eventName, ...args)
  }
}
/**
 * MoveMixin
 * mixin组件功能
 */
export default {
  created() {
    this.__move = new MoveAbility({
      moveHandler: this.moveHandler.bind(this),
      moveUpHandler: this.moveUpHandler.bind(this)
    })
  },
  methods: {
    // 移动开始回调
    startMoveHandler: emitHandler('startMoveHandler'),
    // 移动中回调
    moveHandler: emitHandler('moveHandler'),
    // 移动结束回调
    moveUpHandler: emitHandler('moveUpHandler'),
    getMove() {
      return this.__move
    }
  },
  mounted() {
    const fn = (event) => {
      event.stopPropagation()
      this.startMoveHandler(event)
      this.__move.start(event)
    }

    this.__off = addNativeEvent(this.$el, 'mousedown', fn)
  },
  beforeDestroy() {
    this.__off()
    this.__move.destroy()
    this.__off =
    this.__move =
    null
  }
}

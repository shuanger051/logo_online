import Emitter from '@h5Designer/core/events/Emitter'
import { withlock } from '@h5Designer/utils'
import Vue from 'vue'
import { cloneDeep } from 'lodash'

/**
 * 固定尺寸的缓存
 */
class SizeCache extends Emitter {
  constructor(options = {}) {
    super()
    this.maxSize = options.maxSize
    this.pools = []
  }
  push(source) {
    this.pools.push(source)
    this.emit('add')
    if (this.pools.length > this.maxSize) {
      this.pools.shift()
      this.emit('overflow')
    }
  }
  pop() {
    return this.pools.pop()
  }
  indexOf(index) {
    if (index == null) {
      index = this.pools.length - 1
    }
    return this.pools[index]
  }
  get length() {
    return this.pools.length
  }
}

export default function undoRedo(args = {}) {
  var {
    ignoreAction = [],
    recordList = [],
    maxSize = 100
  } = args

  var firstPoint = null
  var secondPoint = null

  let vm = withlock(
    new Vue({
      data() {
        return {
          undo: new SizeCache({ maxSize: maxSize * 2 }),
          redo: new SizeCache({ maxSize: maxSize })
        }
      }
    })
  )
  /**
   * 记录当前的状态
   * @param {*} state
   * @param {*} callback
   */
  let recordState = (state, callback) => {
    let ret = {}
    let lists = Object.keys(state.cms)
    for (let i = 0; i < lists.length; i++) {
      let key = lists[i]
      if (recordList.indexOf(key) != -1) {
        ret[key] = cloneDeep(state.cms[key])
      }
    }
    callback(ret)
  }

  return (store) => {
    let currentState = null
    let { undo } = vm
    // 当超过限制，重置缓存点的值
    undo.on('overflow', () => {
      firstPoint = secondPoint
      secondPoint = null
      undo.pools.splice(0, maxSize - 1)
    })
    // 缓存添加数据时， 计算缓存点
    undo.on('add', () => {
      if (firstPoint == null) {
        recordState(currentState, (ret) => {
          firstPoint = ret
        })
      } else if (
        undo.length == maxSize + 1
      ) {
        recordState(currentState, (ret) => {
          secondPoint = ret
        })
      }
    })

    store.subscribeAction((action, state) => {
      if (vm.islocked()) {
        return
      }
      let flag = ignoreAction.some((fn) => {
        return fn(action, undo)
      })
      if (action.payload && action.payload.ignore) {
        flag = true
      }
      if (!flag) {
        currentState = state
        undo.push(cloneDeep(action))
      }
    })

    store.$$redo = () => {
      if (!store.$$canRedo) {
        return false
      }
      let { redo, undo } = vm
      vm.dolocked(() => {
        let action = redo.pop()
        store.dispatch(action.type, action.payload)
        undo.push(action)
      })
    }

    store.$$undo = () => {
      if (!store.$$canUndo) {
        return false
      }
      let { redo, undo } = vm
      vm.dolocked(() => {
        // 回退到锚点
        let useSecond = undo.length > maxSize && secondPoint
        store.dispatch('cms/setWorksState', useSecond ? secondPoint : firstPoint)
        // action开始位置
        let action = undo.pop()
        redo.push(action)
        let index = useSecond ? maxSize - 1 : 0

        for (; index < undo.length; index++) {
          let action = undo.indexOf(index)
          store.dispatch(action.type, action.payload)
        }
      })
    }

    store.$$init = () => {
      firstPoint = secondPoint = null
      vm.undo.pools.length = vm.redo.pools.length = 0
    }

    Object.defineProperty(store, '$$canRedo', {
      get() { return vm.redo.length }
    })

    Object.defineProperty(store, '$$canUndo', {
      get() { return vm.undo.length }
    })

    Object.defineProperty(store, '$$redoList', {
      get() { return vm.redo }
    })

    Object.defineProperty(store, '$$undoList', {
      get() { return vm.undo }
    })
  }
}

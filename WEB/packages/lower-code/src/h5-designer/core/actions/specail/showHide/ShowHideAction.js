import systemSign from '@h5Designer/core/events/systemSign'
import BaseAction from '../BaseAction'
import {animation} from '@h5Designer/utils'
/**
 * 显示隐藏action
 */
const {
  created,
  beforeDestroy
} = systemSign.component

const animationName = ['fadeIn', 'fadeOut']

const makeEventOn = (that) => {
  let {
    eventController
  } = that.ctx

  return (sign, cmd) => {
    eventController.on(sign, cmd)
    // 加入action依赖池
    that.dep.add(() => {
      eventController.off(sign, cmd)
    })
  }
}

export default class ShowHideAction extends BaseAction {
  bind() {
    let eventOn = makeEventOn(this)
    let {target} = this.options
    eventOn(target + '_' + created, (vm) => {
      if (vm) {
        this.__targetVm = vm
      }
    })

    eventOn(target + '_' + beforeDestroy, () => {
      this.__targetVm = null
      // 目标元素销毁时，销毁当前的action
      this.destroy()
    })
  }
  unbind() {}

  showHandler(vm, status, done) {
    if (vm.hide == status) {
      done()
    } else {
      if (status === 2) {
        status = 1 - vm.hide
      }
      // 先显示元素
      vm.hide = 0
      animation(vm.$el, [{
        type: animationName[status]
      }], () => {
        done()
        if (vm.hide !== status) {
          if (this.__targetVm.objProperty && this.__targetVm.objProperty.uuid) {
            const id = this.__targetVm.objProperty.uuid
            const target = document.getElementById(id)
            if (target) {
              if (target.tagName == 'VIDEO' || target.tagName == 'AUDIO') {
                target.pause()
              }
            }
          }
          vm.hide = status
        }
      })
    }
  }

  exec(done) {
    // showStatus
    // 1: 显示 2: 隐藏 3: 切换
    let {
      showStatus
    } = this.params
    if (
      this.__targetVm &&
      this.__targetVm.$parent
    ) {
      this.showHandler(this.__targetVm.$parent, showStatus - 1, done)
    } else {
      done()
    }
  }
}

import Emitter from '@h5Designer/core/events'
import MoveAbility from './MoveAbility'
/**
 * MoveEmitter
 */
class MoveEmitter extends Emitter {
  constructor(options) {
    super(options)
    this.__move = new MoveAbility({
      moveUpHandler: (event) => {
        this.emit('moveUp', event)
      },
      moveHandler: (...args) => {
        this.emit('move', ...args)
      }
    })
  }
  destroy() {
    this.move.destroy()
    this.off()
  }

  get move() {
    return this.__move
  }
}

export default MoveEmitter

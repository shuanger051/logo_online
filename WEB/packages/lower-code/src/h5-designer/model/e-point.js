
import { MoveMixin } from './ability'
import { positionPoint } from './ability/constant'

export default {
  name: 'e-point',
  mixins: [ MoveMixin ],
  props: [ 'direction' ],
  mounted() {
    let move = this.getMove()
    move.setDirection({
      direction: positionPoint[this.direction]
    })
  },
  render() {
    return (
      <div
        class = {['u-scale-point', this.direction]}
      >
      </div>
    )
  }
}

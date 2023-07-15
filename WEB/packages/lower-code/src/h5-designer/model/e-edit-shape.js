import ePoint from './e-point'
import { constant, MoveMixin } from './ability'
import { partial } from 'lodash'
import Vue from 'vue'

const { positionPointArray } = constant
const emitHandler = (eventName) => {
  return function (...args) {
    return this.$emit(eventName, ...args)
  }
}
/**
 * shape 构造者
 * @returns
 */
const makeShapeConstructor = () => {
  return {
    name: 'e-shape',
    components: { ePoint },
    props: {
      active: {
        type: Boolean,
        default: false
      },
      selected: {
        type: Boolean,
        default: false
      },
      canScale: {
        type: Boolean,
        default: true
      },
      scalePoint: {
        type: Array
      },
      id: {
        type: String
      }
    },
    methods: {
      pointMoveUpHandler: emitHandler('pointMoveUpHandler'),
      pointStartMoveHandler: emitHandler('pointStartMoveHandler'),
      pointMoveHandler: emitHandler('pointMoveHandler'),
      dblclickHandler(e) {
        this.$emit('dblclickFunc', e)
      },
      clickHandler(e) {
        this.$emit('clickFunc', e)
      }
    },
    render() {
      let pointArray = this.scalePoint || positionPointArray
      return (
        <div
          class= {{
            'active': this.active,
            'u-edit-shape': true,
            'selected': this.selected
          }}
          id={this.id}
          onDblclick = {(e) => {
            this.dblclickHandler(e)
          }}
          onClick={(e) => {
            this.clickHandler(e)
          }}
        >
          {
            this.active && this.canScale ?
              pointArray.map((item, i) => {
                let { key } = item
                return (
                  <ePoint
                    direction = { key }
                    onMoveUpHandler = { partial(this.pointMoveUpHandler, key) }
                    onStartMoveHandler = { partial(this.pointStartMoveHandler, key) }
                    onMoveHandler = { partial(this.pointMoveHandler, key)}
                  ></ePoint>
                )
              }) : null
          }
          { this.$slots.default }
        </div>
      )
    }
  }
}

const moveWrap = (objOrFn) => {
  let ctr = Vue.extend(objOrFn)
  return ctr.extend({
    mixins: [ MoveMixin ],
    props: ['direction'],
    mounted() {
      let move = this.getMove()
      // console.log(move)
      move.setDirection(this.direction)
    }
  })
}

// 可移动的shape
const MoveShap = moveWrap(makeShapeConstructor())
// 不能移动的shape
const NomalShap = makeShapeConstructor()

export default {
  inheritAttrs: false,
  props: {
    canMove: {
      type: Boolean,
      default: true
    }
  },
  render(h) {
    const renderShap = (comp) => {
      return h(
        comp,
        {
          attrs: this.$attrs,
          on: this.$listeners
        },
        this.$slots.default
      )
    }
    return (
      this.canMove ?
        renderShap(MoveShap) :
        renderShap(NomalShap)
    )
  }
}

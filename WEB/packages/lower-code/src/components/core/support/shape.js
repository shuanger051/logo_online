import animationMixin from 'core/mixins/animation.js'
import { relative } from 'path'

/**
 * #!zh: 上下左右 对应的 东南西北
 * #!en: top(north)、bottom(south)、left(west)、right(east)
 */
const directionKey = {
  t: 'n',
  b: 's',
  l: 'w',
  r: 'e'
}

// #!zh: 四个边角、两条中线上的点
const points = ['lt', 'rt', 'lb', 'rb', 'l', 'r', 't', 'b']

export default {
  name: 'Shape',
  mixins: [animationMixin],
  props: [
    'defaultPosition',
    'active',
    'handleMousedownProp',
    'handleElementMoveProp',
    'handlePointMoveProp',
    'handleElementMouseUpProp',
    'handlePointMouseUpProp',
    'handleRotationProp',
    'element'
  ],
  computed: {
    position() {
      return { ...this.defaultPosition }
    }
  },
  created() {
    this.lastCenterX = 0;
    this.lastCenterY = 0;
  },
  methods: {
    /**
     * 通过方位计算样式，主要是 top、left、鼠标样式
     */
    getPointStyle(point, isWrapElement = true) {
      const pos = this.position
      const top = pos.top // !#zh 减4是为了让元素能够处于 border 的中间
      const left = pos.left
      const height = pos.height
      const width = pos.width
      let hasT = /t/.test(point)
      let hasB = /b/.test(point)
      let hasL = /l/.test(point)
      let hasR = /r/.test(point)
      let newLeft = 0
      let newTop = 0
      if (point.length === 2) {
        newLeft = hasL ? 0 : width
        newTop = hasT ? 0 : height
      } else {
        // !#zh 上下点，宽度固定在中间
        if (hasT || hasB) {
          newLeft = width / 2
          newTop = hasT ? 0 : height
        }
        // !#zh 左右点，高度固定在中间
        if (hasL || hasR) {
          newLeft = hasL ? 0 : width
          newTop = height / 2
        }
      }
      const style = {
        marginLeft: hasL || hasR ? '-3px' : 0,
        marginTop: hasT || hasB ? '-3px' : 0,
        left: `${newLeft + (isWrapElement ? 0 : left)}px`,
        top: `${newTop + (isWrapElement ? 0 : top)}px`,
        cursor:
          point
            .split('')
            .reverse()
            .map(m => directionKey[m])
            .join('') + '-resize'
      }
      return style
    },
    /**
     * !#zh 主要目的是：阻止冒泡
     */
    handleWrapperClick(e) {
      e.stopPropagation()
      e.preventDefault()
    },
    getAngle(x, y) {
      let theta = Math.atan2(y, x); // 正切转弧度
      theta = Math.round((180 / Math.PI) * theta); // 弧度转角度
      if (theta < 0) theta = 360 + theta; // 控制角度在0~360度
      return theta; // 返回角度
    },

    handleRotationMousedown(e) {
      const pos = { ...this.position }
      let startY = e.clientY
      let startX = e.clientX
      let startAngle = pos.angle

      let { top, left, width, height } = this.$el.getBoundingClientRect();
      this.lastCenterX = left + width / 2;
      this.lastCenterY = top + height / 2;
      let move = moveEvent => {
        // !#zh 移动的时候，不需要向后代元素传递事件，只需要单纯的移动就OK
        moveEvent.stopPropagation()
        moveEvent.preventDefault()
        const x = moveEvent.clientX - this.lastCenterX;
        const y = moveEvent.clientY - this.lastCenterY;
        const angle = (this.getAngle(x, y) + 90) % 360

        this.handleRotationProp(angle)
      }

      let up = moveEvent => {
        document.removeEventListener('mousemove', move, true)
        document.removeEventListener('mouseup', up, true)
      }
      document.addEventListener('mousemove', move, true)
      document.addEventListener('mouseup', up, true)
    },
    mousedownForMark(point, downEvent) {
      downEvent.stopPropagation()
      downEvent.preventDefault() // Let's stop this event.
      const pos = { ...this.position }
      let height = pos.height
      let width = pos.width
      let top = pos.top
      let left = pos.left
      let startX = downEvent.clientX
      let startY = downEvent.clientY
      let move = moveEvent => {
        let currX = moveEvent.clientX
        let currY = moveEvent.clientY
        let disY = currY - startY
        let disX = currX - startX
        let hasT = /t/.test(point)
        let hasB = /b/.test(point)
        let hasL = /l/.test(point)
        let hasR = /r/.test(point)
        let newHeight = +height + (hasT ? -disY : hasB ? disY : 0)
        let newWidth = +width + (hasL ? -disX : hasR ? disX : 0)
        pos.height = newHeight > 0 ? newHeight : 0
        pos.width = newWidth > 0 ? newWidth : 0
        pos.left = newWidth > 0 ? +left + (hasL ? disX : 0) : pos.left
        pos.top = newHeight > 0 ? +top + (hasT ? disY : 0) : pos.top
        this.handlePointMoveProp(pos, point)
      }
      let up = () => {
        this.handlePointMouseUpProp()
        document.removeEventListener('mousemove', move)
        document.removeEventListener('mouseup', up)
      }
      document.addEventListener('mousemove', move)
      document.addEventListener('mouseup', up)
    },
    /**
     * !#zh 给 当前选中元素 添加鼠标移动相关事件
     *
     * @param {mouseEvent} e
     */
    mousedownForElement(e) {
      const pos = { ...this.position }
      let startY = e.clientY
      let startX = e.clientX
      let startTop = pos.top
      let startLeft = pos.left

      let move = moveEvent => {
        // !#zh 移动的时候，不需要向后代元素传递事件，只需要单纯的移动就OK
        moveEvent.stopPropagation()
        moveEvent.preventDefault()

        let currX = moveEvent.clientX
        let currY = moveEvent.clientY
        pos.top = currY - startY + startTop
        pos.left = currX - startX + startLeft
        this.handleElementMoveProp(pos)
      }

      let up = moveEvent => {
        this.handleElementMouseUpProp()
        document.removeEventListener('mousemove', move, true)
        document.removeEventListener('mouseup', up, true)
      }
      document.addEventListener('mousemove', move, true)
      document.addEventListener('mouseup', up, true)
    },
    handleMousedown(e) {
      if (this.handleMousedownProp) {
        this.handleMousedownProp()
        this.mousedownForElement(e, this.element)
      }
    },
    /**
     * !#en: delete element with keyboard
     * !#zh: 键盘快捷键删除元素
     *
     */
    handleDeleteByKeyboard(event) {
      const key = event.keyCode || event.charCode
      if (key === 8 || key === 46) {
        this.$emit('delete')
      }
    },
    /**
     * detect key pressed on keyboard
     * 检测键盘按键 按下行为
     *
     * 支持如下行为：
     * - Backspace/Delete 快速删除元素
     */
    handleKeyPressed(e) {
      this.handleDeleteByKeyboard(e)
    }
  },
  render(h) {
    return (
      <div onClick={this.handleWrapperClick}>
        <div
          tabIndex="0"
          onKeydown={this.handleKeyPressed}
          onMousedown={this.handleMousedown}
          class={{ 'shape__wrapper-active': this.active, shape__wrapper: true }}
        >
          {this.active &&
            points.map(point => {
              const pointStyle = this.getPointStyle(point)
              return (
                <div
                  key={point}
                  data-point={point}
                  style={pointStyle}
                  class="shape__scale-point"
                  onMousedown={this.mousedownForMark.bind(this, point)}
                ></div>
              )
            })}
          {this.$slots.default}
        </div>
        {this.active && (
          <div onMousedown={this.handleRotationMousedown} class = "shape_rotation-wrap">
          <div class="shape_rotation-line"></div>
          <div class="shape_rotation-point"></div>
          </div>
        )}
      </div>
    )
  }
}

<template>
  <div style="z-index:9999;width: 100%;position: absolute;margin-bottom: 50px;cursor: ns-resize;"
    @mousedown="handleMousedown">
    <div class="adjust-line"></div>
    <div class="adjust-height-wrap">
      <div class="adjust-button"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'AdjustHeight',
  data() {
    return {
      timer: '',
      startY: ''
    }
  },
  methods: {
    handleMousedown(e) {
      this.startY = e.clientY
      let move = moveEvent => {
        moveEvent.preventDefault()
        moveEvent.stopPropagation()
        if (this.startY - moveEvent.clientY > 0) {
          if (this.timer) {
            clearInterval(this.timer)
          }
          if (this.startY - moveEvent.clientY < 50) {
            this.$emit('adjust-work-height', -1)
          } else {
            this.timer = setInterval(() => {
              this.$emit('adjust-work-height', -2)
            }, 10)
          }
        } else {
          if (this.timer) {
            clearInterval(this.timer)
          }
          if (this.startY - moveEvent.clientY > -50) {
            this.$emit('adjust-work-height', 1)
          } else {
            this.timer = setInterval(() => {
              this.$emit('adjust-work-height', 2)
            }, 10)
          }
        }
      }

      let up = moveEvent => {
        document.removeEventListener('mousemove', move, true)
        document.removeEventListener('mouseup', up, true)
        clearInterval(this.timer)
        this.startY = ''
      }

      document.addEventListener('mousemove', move, true)
      document.addEventListener('mouseup', up, true)
    },
    handleMove(e) {

    },
    handleUp() {

    }
  }
}
</script>
<style lang="scss" scoped>
// .adjust-height-wrap {
//   box-sizing: content-box;
//   height: 23px;
//   line-height: 23px;
//   background: rgba(91, 91, 91, 0.3);
//   color: #ffffff;
//   border: 1px solid #ffffff;
//   cursor: ns-resize;
//   text-align: center;
//   position: absolute;
//   z-index: 999;
//   width: 375px;
//   &:hover {
//     border: 1px solid blue;
//   }
// }
.adjust-line {
  width: 100%;
  height: 1px;
  background-color: #1261ff;
  position: relative;
}
.adjust-height-wrap {
  margin: 0 auto;
  width: 30px;
  height: 10px;
  border-radius: 2px;
  padding-top: 4px;
  background-color: #1261ff;
  position: relative;
  cursor: ns-resize;
  .adjust-button {
    margin: 0 auto;
    width: 10px;
    height: 1px;
    background-color: #ccd5db;
  }
}
</style>

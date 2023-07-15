<template>
  <div class="textarea-wrap">
    <div
      class="mTextarea"
      ref="mTextarea"
      :style="property"
      :contenteditable="editState"
      @keydown="keydownHandler"
      @blur="blurHandler"
      @mouseup="mouseupHandler"
      @mousedown="mousedownHandler"
      @mousemove="mousemoveHandler"
      :placeholder="placeholder"
      :class="[editState ? 'edit' : '']"
      v-html="content"
    ></div>
  </div>
</template>
<script>
import { cloneDeep, isEmpty } from 'lodash'
export default {
  props: {
    // 内容, 非必传
    value: String,
    // 输入提示，非必传
    placeholder: String,
    // 样式属性
    property: Object,
    // 编辑状态
    editState: Boolean,
    // 是否激活
    active: Boolean | String,
    // 组件高度
    height: Number | String,
    // 字体高度
    fontHeight: Number | String
  },
  data() {
    return {
      content: '', // 文字内容
      isMove: false, // 是否移动
      moveX: 0,
      moveY: 0,
      heightPX: 0,
      scrollHeight: 0
    }
  },
  watch: {
    value: {
      handler(val) {
        this.init(val)
      },
      deep: true
    },
    active: {
      handler(val) {
        // 监听文本当前选中状态
        if (!val) {
          this.blurHandler()
        } else {
          this.content = this.value ? decodeURIComponent(cloneDeep(this.value)) : ''
        }
      },
      deep: true
    },
    height: {
      handler(val) {
        const num = val ? cloneDeep(val) : 0
        this.heightPX = Number(num)
      },
      deep: true
    }
  },
  mounted() {
    this.init(this.value)
    const num = this.height ? cloneDeep(this.height) : 0
    this.heightPX = Number(num)
  },
  methods: {
    // 初始化赋值
    init(value) {
      const text = cloneDeep(value)
      this.content = text ? decodeURIComponent(text) : ''
      this.scrollHeight = cloneDeep(this.$refs.mTextarea.scrollHeight)
    },
    // 按键事件
    keydownHandler(event) {
      var key = event.keyCode || event.charCode
      // if (key == 8 || key == 46 || (key >= 37 && key <= 40)) {
      //   // 8:BackSpace 46:delete， key >= 37 && key <= 40
      //   event.stopPropagation()
      // }
      if (key == 13) {
        if (this.scrollHeight != this.$refs.mTextarea.scrollHeight) {
          this.scrollHeight = cloneDeep(this.$refs.mTextarea.scrollHeight)
          this.heightPX = this.$refs.mTextarea.scrollHeight
          this.$emit('editStyleFunc', this.heightPX)
        }
        // this.heightPX = this.heightPX + (this.fontHeight * this.property['line-height'])
      }

      if (key == 8 || key == 46) {
        console.log(this.$refs.mTextarea.scrollHeight, this.$refs.mTextarea.offsetHeight, this.$refs.mTextarea.scrollTop)
      }

      event.stopPropagation()
    },
    // 失焦事件
    blurHandler() {
      // 失去焦点
      const innerHTML = this.$refs.mTextarea.innerHTML
      const content = encodeURIComponent(cloneDeep(innerHTML))
      let update = false
      if (content != encodeURIComponent(this.content)) {
        update = true
      }
      this.$emit('editChange', {content, update})
      // 移除范围
      window.getSelection().removeAllRanges()
      // 滑到顶部
      this.$refs.mTextarea.scroll({ top: 0, left: 0, behavior: 'smooth' })
    },
    // 获取焦点
    focusHandler() {
      // 获取焦点
      this.$nextTick(() => {
        let obj = this.$refs.mTextarea
        obj.focus() // 解决ff不获取焦点无法定位问题
        this.selectText(this.$refs.mTextarea)
        this.$refs.mTextarea.spellcheck = false
      })
    },
    // 选中
    selectText(element) {
      const range = document.createRange()
      range.selectNodeContents(element)
      window.getSelection().removeAllRanges()
      window.getSelection().addRange(range)
    },
    // 鼠标抬起
    mouseupHandler() {
      this.isMove = false
    },
    // 鼠标按下
    mousedownHandler(event) {
      if (this.editState) {
        event.stopPropagation()
      }
      this.isMove = true
    },
    // 鼠标移动
    mousemoveHandler(event) {
      if (this.isMove && this.editState) {
        event.stopPropagation()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.textarea-wrap {
  height: 100%;
  border-radius: 2px;
  font-size: 0;
  &:active {
    background: transparent;
  }
  .mTextarea {
    width: 100%;
    height: 100%;
    display: inline-block;
    outline: none;
    overflow-y: auto;
    word-wrap : break-word;
    flex-direction: column;
    &:empty:before {
      content: attr(placeholder);
      font-size: 14px;
      color: #999;
      letter-spacing: 1px;
      font-weight: normal;
      font-style: normal;
      text-align: left;
      width: 100%;
      height: 100%;
      display: inline-flex;
      align-items: flex-start;
      justify-content: flex-start;
    }

    &:empty {
      text-decoration: none !important;
    }
  }
}
</style>

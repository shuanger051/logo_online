<template>
  <div class="textarea-wrap" @mousemove="mousemove($event)" @mouseup="mouseup($event)">
    <div class="mTextarea" id="text_content" ref="mTextarea" :style="property" :contenteditable="edit"
      @keydown="keydownHandler" @dblclick="dbclick(false, $event)" @blur="blur(true, $event)" @input="inputHandle" @compositionstart="compositionstartHandle" @compositionend="compositionendHandle"
      :placeholder="placeholder" :class="{ edit: edit }" v-text="content"></div>
    <!-- <h-tinymce-editor v-model="value"
        :initValue="value"
        :menubar="false"
        height="100%"
        :config="{convert_urls: false}" /> -->
  </div>
</template>
<script>
import { cloneDeep } from 'lodash'
export default {
  props: {
    context: Object,
    // 输入提示，非必传
    placeholder: String,
    // 最大字数，非必传
    maxlength: {
      type: Number,
      default: 99999
    },
    // 行数，非必传
    rows: {
      type: Number,
      default: 6
    },
    // 字体大小，非必传
    fontSize: {
      type: Number,
      default: 28
    },
    // 行高，非必传
    lineHeight: {
      type: Number,
      default: 1.4
    },
    // 值，必传
    value: String,
    disabled: {
      type: Boolean,
      default: false
    },
    property: {
      type: Object
    },
    uuid: String,
    active: Boolean | String
  },
  computed: {
    style() {
      return {
        fontSize: this.fontSize / 75 + 'rem',
        lineHeight: this.lineHeight,
        maxWidth: '100%'
        // maxHeight: this.rows * this.fontSize / 75 * this.lineHeight + 'rem'
      }
    }
  },
  data() {
    return {
      flag: true,
      contenteditable: false,
      mTextarea: null,
      content: this.value,
      edit: false,
      selected: false
    }
  },
  components: {
  },
  watch: {
    value: {
      handler(val) {
        this.content = cloneDeep(val)
      },
      deep: true
    },
    active: {
      handler(val) {
        // 监听文本当前选中状态
        if (!val) {
          this.blur(true)
        }
      },
      deep: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.mTextarea = this.$refs.mTextarea
      // 回显
      // this.mTextarea.innnerHTML = this.value
    })
  },
  methods: {
    keydownHandler(event) {
      var key = event.keyCode || event.charCode
      if (key == 8 || key == 46) {
        event.stopPropagation()
      }
      if (key == 13) {
        // 禁用换行
        event.stopPropagation()
        event.cancelBubble = true
        event.preventDefault()
        window.getSelection().removeAllRanges()
        this.$refs.mTextarea.blur()
      }
      if (key == 37) {
        event.stopPropagation()
      }
      if (key == 39) {
        event.stopPropagation()
      }
    },
    // 修改contenteditable属性
    dbclick(item, e) {
      e.preventDefault()
      e.stopPropagation()
      e.cancelBubble = true
      item ? (this.edit = false) : (this.edit = true)
      if (this.edit === true) {
        this.$nextTick(function() {
          this.selectText(this.$refs.mTextarea)
          this.selected = true
          this.$refs.mTextarea.spellcheck = false
          this.context.updateElementProperty({ cantMove: true })
        })
      }
      this.$refs.mTextarea.click()
    },
    blur(item, e) {
      item ? (this.edit = false) : (this.edit = true)
      if (this.edit === true) {
        this.$nextTick(function() {
          this.$refs.mTextarea.focus()
          this.$refs.mTextarea.spellcheck = false
        })
      }
      this.$refs.mTextarea.click()
      // if (e) {
      //   setTimeout(() => {
      //     const content = this.$refs.mTextarea.innerText
      //     this.context.updateElementProperty({ content: content, uuid: this.uuid, cantMove: false })
      //   }, 500)
      // }
      setTimeout(() => {
        const content = this.$refs.mTextarea.innerText
        this.context.updateElementProperty({ content: content, uuid: this.uuid, cantMove: false })
      }, 500)
    },
    mousemove(event) {
      if (this.edit) {
        event.stopPropagation()
        event.cancelBubble = true
      }
    },
    mouseup(event) {
      if (this.edit) {
        event.stopPropagation()
        event.cancelBubble = true
      }
    },
    // 选中
    selectText(element) {
      const range = document.createRange()
      range.selectNodeContents(element)
      window.getSelection().removeAllRanges()
      window.getSelection().addRange(range)
    },
    inputHandle(event) {
      if (this.flag) {
        if (event.target.innerText.length >= this.maxlength) {
          event.target.innerText = event.target.innerText.slice(0, this.maxlength)
          this.blur(true)
        }
      }
    },
    compositionstartHandle(event) {
      this.flag = false
    },
    compositionendHandle(event) {
      this.flag = true
      if (event.target.innerText.length >= this.maxlength) {
        event.target.innerText = event.target.innerText.slice(0, this.maxlength)
        this.blur(true)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.textarea-wrap {
  /* background: #f8f8f8; */
  border-radius: 2px;
  font-size: 0;
  &:active {
    background: #eee;
  }
  .mTextarea {
    width: 100%;
    height: 100%;
    display: inline-block;
    outline: none;
    overflow: auto;
    // 加了可实现纯textarea功能，不加则像富文本一样可以写入css样式和HTML标签
    // 必须加，不然移动端有些浏览器无光标
    user-select: auto;
    -webkit-user-select: auto;
    // placeholder属性
    &:empty:before {
      content: attr(placeholder);
      font-size: 12px;
      color: #999;
      letter-spacing: 0px;
      font-weight: normal;
      font-style: normal;
      text-align: left;
      width: 100%;
      height: 100%;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
    &:empty {
      text-decoration: none !important;

    }
  }
  .mTextarea.edit {
    user-modify: read-write-plaintext-only;
    -webkit-user-modify: read-write-plaintext-only;
  }
}
/deep/ .tox-editor-header {
  display: none;
}
</style>

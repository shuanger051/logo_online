<template>
  <div class="text-content">
    <div class="text-box" v-if="mode === 'edit'" @dblclick="dblFunc">
      <text-editor
        ref="textEditor"
        placeholder='双击编辑文本'
        :maxlength='255'
        :value="property.content"
        :property="{
          ...objProperty
        }"
        :height="style.height"
        :fontHeight="property['font-size']"
        :editState="edit"
        :isNowrap="false"
        :active="active"
        @editChange="editChange"
        @editStyleFunc="editStyleFunc"
      />
    </div>
    <div class="text-box" v-else>
      <div
        class="text-inner-html"
        widget-name="hs-cms-text"
        :style="{
          ...objProperty,
          'overflow-y': 'auto'
        }"
        v-html="decodeURIComponent(property.content)"
      >
      </div>
    </div>
  </div>
</template>

<script>

import textEditor from '@Components/TextBoxEditor'
import { mapValues } from 'lodash'
export default {
  props: ['name', 'context', 'property', 'style', 'uuid', 'active'],
  data() {
    return {
      contenteditable: false,
      objProperty: {},
      mode: '',
      value: '',
      edit: false
    }
  },
  components: {
    textEditor
  },
  created() {
    this.mode = this.context.mode
    this.init(this.property)
  },
  watch: {
    property: {
      handler(val) {
        this.init(val)
      },
      deep: true
    }
  },
  methods: {
    // 初始化
    init(property) {
      this.objProperty = mapValues(property, (value, key) => {
        if (
          key === 'font-size' ||
          key === 'letter-spacing' ||
          key === 'padding-left' ||
          key === 'padding-right' ||
          key === 'padding-top' ||
          key === 'padding-bottom'
        ) {
          return value + 'px'
        }
        return value
      })
      // if (this.context.mode === 'preview') {
      //   Object.assign(this.objProperty, this.style)
      // }
    },
    // 更新值
    editChange(data) {
      let { updateElementProperty } = this.context
      if (data.update) {
        updateElementProperty({ content: data.content, uuid: this.uuid })
      }
      this.editStateFunc(false)
    },
    editStyleFunc(value) {
      let { updateElementStyle } = this.context
      updateElementStyle({height: value})
    },
    // 双击
    dblFunc() {
      this.editStateFunc(true)
      this.$refs.textEditor.focusHandler()
    },
    editStateFunc(item) {
      this.edit = item
    }
  }
}
</script>
<style scoped lang="scss">
.text-content .text-box {
  height: 100%;
}

.text-inner-html {
  height: 100%;
  word-wrap : break-word;
  flex-direction: column;
}
</style>

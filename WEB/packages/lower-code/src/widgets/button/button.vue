<template>
  <div class="button-content">
    <!-- <div
      v-if="context.mode === 'edit'"
      class="button-edit-content"
      widget-name="hs-cms-button"
      ref="context"
      :style="{
        ...objProperty
      }"
      @keydown="keydownHandler"
      @dblclick="convertEditStatus()"
      :contenteditable="contenteditable"
      @blur="convertEditStatus(true, $event)"
    >
      {{ property.content }}
    </div> -->
    <div v-if="mode === 'edit'">
      <text-editor placeholder='请输入文字' :uuid="uuid" :maxlength='255' :context="context" :value="property.content" :active="active" :property="{
          ...objProperty
        }"/>
    </div>
    <div
      v-else
      widget-name="hs-cms-button"
      :style="{
        ...objProperty
      }"
      class="button-preview-content"
      :class="{'suction-bottom': suctionBottomFlag}"
    >
      {{ property.content }}
    </div>
  </div>
</template>

<script>
import { mapValues } from 'lodash'
import textEditor from '@Components/textEditor'
export default {
  props: ['name', 'context', 'property', 'style', 'uuid', 'active'],
  data() {
    return {
      contenteditable: false,
      objProperty: {},
      mode: '',
      suctionBottomFlag: false
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
    init(property) {
      let objProperty = mapValues(property, (value, key) => {
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
      if (objProperty['button-type'] === 'normal') {
      } else if (objProperty['button-type'] === 'suction-bottom') {
        this.suctionBottomFlag = true
      }
      if (objProperty['background-image']) {
        objProperty.backgroundImage = `url(${objProperty['background-image']})`
        objProperty.backgroundColor = `${objProperty['background-color']}`
        objProperty.backgroundRepeat = 'no-repeat'
        objProperty.backgroundSize = '100% 100%'
        // objProperty.background = `url(${objProperty['background-image']}) ${objProperty['background-color']}`
      } else {
        objProperty.background = `${this.objProperty['background-color']}`
      }
      if (this.context.mode === 'preview') {
        this.style['background-color'] = objProperty['background-color']
        Object.assign(objProperty, this.style)
      }
      this.objProperty = objProperty
      console.log(objProperty)
    },
    // 修改contenteditable属性
    convertEditStatus(item, e) {
      item ? (this.contenteditable = false) : (this.contenteditable = true)
      this.$refs.context.click()
      let content
      if (e) {
        content = e.target.innerHTML
        this.context.updateElementProperty({ content: content })
      }
    },
    keydownHandler(event) {
      var key = event.keyCode || event.charCode
      if (key == 8 || key == 46) {
        event.stopPropagation()
      }
    }
  }
}
</script>
<style scoped lang="scss">
.button-edit-content, .button-preview-content {
  outline-style: none;
  user-select: none;
}
.button-preview-content {
  position: absolute;
}
.button-preview-content.suction-bottom {
  position: sticky;
  top: 812px;
}
.textarea-wrap {
  height: 100%;
  .mTextarea {
    width: 100%;
    height: 100%;
    display: inline-block;
    outline: none;
    overflow: auto;
    user-select: auto;
    -webkit-user-select: auto;
    // placeholder属性
    &:empty:before {
      content: attr(placeholder);
      font-size: 28/75rem;
      color: #999;
    }
  }
}
.button-content {
  height: 100%;
  & > div {
    height: 100%;
  }
}
</style>

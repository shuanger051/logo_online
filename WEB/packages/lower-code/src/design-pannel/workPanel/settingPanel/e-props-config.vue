<template>
  <div class="right-panel-container props-config">
    <h-input placeholder="组件名称" :maxlength="20" :filterRE="/[<>]/g" v-model="elementDataProxy.element_name"></h-input>
    <h-form>
      <PropsConfig :editorPropsConfig="editorPropsConfig" :element_name="element_name" :elements="items"></PropsConfig>
    </h-form>
    <!-- 组件设置 -->
    <collapse-wrap :name="name" v-if="(!positionSetHide || !sizeSetHide) && !castratedVersion">
      <h-row :style="{'margin-top': '8px'}" class="common-style-panel" v-if="!sizeSetHide">
        <h-col :span="3">
          尺寸
        </h-col>
        <h-col :span="10">
          <h-input-number size="small" :precision="0" setzero :min="0" v-model="elementStyleValue['width']"
            @on-change="setEle('width', $event)" :readonly="widthSetDisabled">
            <div slot="append" class="slot-append-text">W</div>
          </h-input-number>
        </h-col>
        <h-col :span="10">
          <h-input-number size="small" setzero :precision="0" :min="0" v-model="elementStyleValue['height']"
            @on-change="setEle('height', $event)" :readonly="heightSetDisabled">
            <span slot="append" class="slot-append-text">H</span>
          </h-input-number>
        </h-col>
      </h-row>
      <h-row :style="{'margin-top': '8px'}" class="common-style-panel" v-if="!positionSetHide">
        <h-col :span="3">
          位置
        </h-col>
        <h-col :span="10">
          <h-input-number :readonly="leftSetDisabled" size="small" v-model="elementStyleValue['left']"
            @on-change="setEle('left', $event)">
            <span slot="append" class="slot-append-text">L</span>
          </h-input-number>
        </h-col>
        <h-col :span="10" v-if="elementStyleValue['top'] !== 'auto'">
          <h-input-number size="small" :readonly="elementStyleValue['top'] === 'auto' || topSetDisabled"
            v-model="elementStyleValue['top'] " @on-change="setEle('top', $event)">
            <span slot="append" class="slot-append-text">T</span>
          </h-input-number>
        </h-col>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="边框样式" v-if="!borderSetHide">
      <h-form>
        <h-row>
          <h-col :span="5">
            <ColorSelect padding="5px 7px" icon="plus-round" :selectedColor="elementProxy['border-color']"
              emptyHeight="26" emptyWidth="26" placement="right" @updateColor="setEle('border-color', $event)"
              @resetColor="setEle('border-color', 'rgba(0,0,0,1)')" />
          </h-col>
          <h-col :span="9">
            <h-input-number :precision="0" size="small" :min="0" :max="255" setzero
              v-model="elementStyleValue['border-width']" @on-change="setEle('border-width', $event, true)">
            </h-input-number>
          </h-col>
          <h-col :span="4">
            <span class="iconfont icon-jurassic_border-none" @click="confirm"></span>
          </h-col>
          <h-col :span="4">
            <h-input-number :precision="0" size="small" :min="0" :max="255" setzero
              v-model="elementStyleValue['border-radius']" @on-change="setEle('border-radius', $event, true)">
            </h-input-number>
          </h-col>
        </h-row>
        <h-row>
          <h-col :span="3">
            <p class="font-desc">颜色</p>
          </h-col>
          <h-col :span="10">
            <p class="font-desc">线宽</p>
          </h-col>
          <h-col :span="4">
            <p class="font-desc">可见性</p>
          </h-col>
          <h-col :span="7">
            <p class="font-desc">圆角</p>
          </h-col>
        </h-row>
      </h-form>
    </collapse-wrap>
    <collapse-wrap name="背景色与透明度" v-if="!backgroundColorSetHide">
      <ColorSelect padding="3px 5px" icon="plus-round" :selectedColor="elementStyleValue['background-color']"
        :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000']" preHeight="20px" preWidth="20px" placement="right"
        :needTransparency="true" @updateColor="setEle('background-color', $event)" />
    </collapse-wrap>
    <h-msg-box v-model="modal" :mask-closable="false" title="点击选择要显示的边框">
      <div class="border-select" :style="{
          'border-left-width': elementStyleValue['border-left-style'] === 'none' ? 0 : '1px',
          'border-right-width': elementStyleValue['border-right-style'] === 'none' ? 0 : '1px',
          'border-top-width': elementStyleValue['border-top-style'] === 'none' ? 0 : '1px',
          'border-bottom-width': elementStyleValue['border-bottom-style'] === 'none' ? 0 : '1px',
        }">
        <div class="l" @click="toggle('border-left-style', elementStyleValue['border-left-style'])"></div>
        <div class="t" @click="toggle('border-top-style', elementStyleValue['border-top-style'])"></div>
        <div class="r" @click="toggle('border-right-style', elementStyleValue['border-right-style'])"></div>
        <div class="b" @click="toggle('border-bottom-style', elementStyleValue['border-bottom-style'])"></div>
      </div>
      <div slot="footer"></div>
    </h-msg-box>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import CollapseWrap from '@Components/collapseWrap'
import ColorSelect from '@Components/ColorSelect'

import '@Root/styles/props-config.scss'
import '@Assets/font/iconfont.css'
import PropsConfig from './props'
export default {
  name: 'ePropsConfig',
  props: {
  },
  computed: {
    ...mapGetters('cms/elements', ['selectedPageElements']),
    ...mapState('cms/editState', [
      'selectedPage',
      'selectedElement'
    ]),
    ...mapGetters('cms/editState', ['selectedElementData']),
    ...mapState('cms/elements', ['items']),
    elementDataProxy() {
      return new Proxy(this.selectedElementData, {
        get: (target, name) => {
          return this.selectedElementData[name] || ''
        },
        set: (target, name, value) => {
          this.updateElementName({ 'element_name': value })
          return true
        }
      })
    },
    elementProxy() {
      return new Proxy(this.selectedElementData.style, {
        get: (target, name) => {
          // console.log(name, this.selectedElementData.style)
          return this.selectedElementData.style['border-color'] || ''
        },
        set: (target, name, value) => {
          if (name === 'border-color' && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if (name === 'border-color' && !value) {
            value = ''
          }
          // console.log(name, value)
          if (Object.prototype.toString.call(name) === '[object Object]') {
            this.updateElementStyle({
              [name]: value
            })
          } else {
            this.updateElementStyle({
              [name]: value
            })
          }
          return true
        }
      })
    }
  },
  watch: {
    selectedElement: {
      handler(newValue, oldValue) {
        this.positionChange()
        let arr = this.items[this.selectedPage].filter(e => {
          return e.uuid === this.selectedElement
        })
        if (arr && arr.length > 0) {
          this.elementStyleValue = arr[0].style
          this.elementStyleValue['border-radius'] = parseInt(this.elementStyleValue['border-radius'])
          this.elementStyleValue['border-width'] = parseInt(this.elementStyleValue['border-width'])
        }
      },
      deep: true
    },
    items: {
      handler(newValue, oldValue) {
        this.positionChange()
        let arr = this.items[this.selectedPage].filter(e => {
          return e.uuid === this.selectedElement
        })
        // console.log(this.selectedElement, arr, 'arr')]
        if (arr && arr.length > 0) {
          this.elementStyleValue = arr[0].style
          this.elementStyleValue['border-radius'] = parseInt(this.elementStyleValue['border-radius'])
          this.elementStyleValue['border-width'] = parseInt(this.elementStyleValue['border-width'])
        }
      },
      deep: true
    }
  },
  created() {
    let arr = this.items[this.selectedPage].filter(e => {
      return e.uuid === this.selectedElement
    })
    // console.log(this.selectedElement, arr, 'arr')
    if (arr && arr.length > 0) {
      this.elementStyleValue = arr[0].style
      this.elementStyleValue['border-radius'] = parseInt(this.elementStyleValue['border-radius'])
      this.elementStyleValue['border-width'] = parseInt(this.elementStyleValue['border-width'])
    }
    this.positionChange()
  },
  data() {
    return {
      elementStyleValue: null,
      element_name: null,
      context: null,
      modal: false,
      loadCustomEditorFlag: false,
      editorPositionConfig: [
        { type: 'h-input-number', label: '上', key: 'top', value: '0' },
        { type: 'h-input-number', label: '左', key: 'left', value: '0' },
        { type: 'h-input-number', label: '宽', key: 'width', value: '0' },
        { type: 'h-input-number', label: '高', key: 'height', value: '0' }
      ],
      editorPropsConfig: [
        {
          type: 'h-input-number',
          label: '字号',
          key: 'font-size',
          value: '12px'
        },
        {
          type: 'h-input-number',
          label: '字间距',
          key: 'letter-spacing',
          value: '0'
        },
        {
          type: 'h-input-number',
          label: '行间距',
          key: 'line-height',
          value: '1'
        },
        { type: 'color-picker', label: '颜色', key: 'color', value: '#666' },
        { type: 'radio', label: '加粗', key: 'font-weight', value: '400' },
        { type: 'radio', label: '倾斜', key: 'font-style', value: 'normal' },
        {
          type: 'radio',
          label: '下划线',
          key: 'text-decoration',
          value: 'none'
        },
        {
          type: 'radio',
          label: '删除线',
          key: 'text-decoration',
          value: 'none'
        },
        {
          type: 'radio',
          label: '对齐方式',
          key: 'text-align',
          value: 'center'
        },
        {
          type: 'radio',
          label: '上下对齐方式',
          key: 'align-items',
          value: 'center'
        }
      ],
      fontColor: '',
      borderColor: '',
      sizeSetHide: false,
      positionSetHide: false,
      borderSetHide: false,
      backgroundColorSetHide: false,
      widthSetDisabled: false,
      heightSetDisabled: false,
      leftSetDisabled: false,
      topSetDisabled: false,
      name: '尺寸与位置',
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true' // 是否打开Lite版
    }
  },
  components: {
    PropsConfig,
    CollapseWrap,
    ColorSelect
  },
  methods: {
    ...mapActions('cms/elements', ['updateElementStyle', 'updateElementProperty', 'updateElementName']),
    // 设置选中元素通用样式
    setEle(key, value, type) {
      if (key === 'border-color' && !value) {
        value = 'rgba(255, 255, 255, 0)'
      }
      const autoScal = this.selectedElementData.editPanelConfig.autoScal
      const minWidth = this.selectedElementData.editPanelConfig.minWidth
      const minHeight = this.selectedElementData.editPanelConfig.minHeight
      if (autoScal && (key === 'width' || key === 'height')) {
        //宽高1:1缩放
        let minSize = minWidth||minHeight
        let size = value
        if(minSize&&value<minSize){
          size = minSize
          this.updateElementStyle({
            width: size,
            height: size,
            ignore:true
          })
        }else{
          this.updateElementStyle({
            width: size,
            height: size,
          })
        }
      } else {
        //最小宽度 高度设置
        if(minWidth&&value<minWidth&&key==='width'){
          this.updateElementStyle({
            [key]: minWidth,
            ignore:true
          })
        }else if(minHeight&&value<minHeight&&key==='height'){
          this.updateElementStyle({
            [key]: minHeight,
            ignore:true
          })
        }else{
          this.updateElementStyle({
            [key]: value
          })
        }
      }
    },
    // 元素位置移动
    positionChange() {
      this.selectedPageElements.map(e => {
        if (e.uuid === this.selectedElement) {
          let obj = e.style
          let arr = Object.keys(obj)
          let valArr = arr.map(function (i) {
            return obj[i]
          })
          this.element_name = e.name
          let obj1 = e.property
          let arr1 = Object.keys(obj1)
          let valArr1 = arr1.map(function (i) {
            return obj1[i]
          })
          this.editorPositionConfig.map(ele => {
            arr.map((element, i) => {
              if (element === ele.key) {
                this.$set(ele, 'value', valArr[i])
              }
            })
          })
          this.editorPropsConfig.map(ele => {
            arr1.map((element, i) => {
              if (element === ele.key) {
                this.$set(ele, 'value', valArr1[i])
              }
            })
          })
          if (e.editPanelConfig && e.editPanelConfig.sizeSet) {
            if (e.editPanelConfig.sizeSet.hide == true) {
              this.sizeSetHide = true
              this.name = '位置'
            } else {
              this.sizeSetHide = false
              this.name = '尺寸与位置'
            }
          } else {
            this.sizeSetHide = false
            this.name = '尺寸与位置'
          }
          if (e.editPanelConfig && e.editPanelConfig.positionSet) {
            if (e.editPanelConfig.positionSet.hide == true) {
              this.positionSetHide = true
              this.name = '尺寸'
            } else {
              this.positionSetHide = false
            }
          } else {
            this.positionSetHide = false
          }
          if (e.editPanelConfig && e.editPanelConfig.borderSet) {
            if (e.editPanelConfig.borderSet.hide == true) {
              this.borderSetHide = true
            } else {
              this.borderSetHide = false
            }
          } else {
            this.borderSetHide = false
          }
          if (e.editPanelConfig && e.editPanelConfig.backgroundColorSet) {
            if (e.editPanelConfig.backgroundColorSet.hide == true) {
              this.backgroundColorSetHide = true
            } else {
              this.backgroundColorSetHide = false
            }
          } else {
            this.backgroundColorSetHide = false
          }
          if (e.editPanelConfig && e.editPanelConfig) {
            if (e.editPanelConfig.sizeSet) {
              if (e.editPanelConfig.sizeSet.width) {
                if (e.editPanelConfig.sizeSet.width.disabled) {
                  this.widthSetDisabled = true
                } else {
                  this.widthSetDisabled = false
                }
              } else {
                this.widthSetDisabled = false
              }
            } else {
              this.widthSetDisabled = false
            }
          } else {
            this.widthSetDisabled = false
          }
          if (e.editPanelConfig && e.editPanelConfig) {
            if (e.editPanelConfig.sizeSet) {
              if (e.editPanelConfig.sizeSet.height) {
                if (e.editPanelConfig.sizeSet.height.disabled) {
                  this.heightSetDisabled = true
                } else {
                  this.heightSetDisabled = false
                }
              } else {
                this.heightSetDisabled = false
              }
            } else {
              this.heightSetDisabled = false
            }
          } else {
            this.heightSetDisabled = false
          }
          if (e.editPanelConfig && e.editPanelConfig) {
            if (e.editPanelConfig.positionSet) {
              if (e.editPanelConfig.positionSet.left) {
                if (e.editPanelConfig.positionSet.left.disabled) {
                  this.leftSetDisabled = true
                } else {
                  this.leftSetDisabled = false
                }
              } else {
                this.leftSetDisabled = false
              }
            } else {
              this.leftSetDisabled = false
            }
          } else {
            this.leftSetDisabled = false
          }
          if (e.editPanelConfig && e.editPanelConfig) {
            if (e.editPanelConfig.positionSet) {
              if (e.editPanelConfig.positionSet.top) {
                if (e.editPanelConfig.positionSet.top.disabled) {
                  this.topSetDisabled = true
                } else {
                  this.topSetDisabled = false
                }
              } else {
                this.topSetDisabled = false
              }
            } else {
              this.topSetDisabled = false
            }
          } else {
            this.topSetDisabled = false
          }
        }
      })
    },
    // 设置选中元素属性
    setEleprops(key, value, type) {
      let realValue
      if (type) {
        realValue = value
      } else {
        if (Object.prototype.toString.call(key) === '[object Object]') {
          this.updateElementProperty({
            key
          })
          return
        }
        realValue = Number(value) + 'px'
      }
      this.updateElementProperty({
        [key]: realValue
      })
    },
    confirm() {
      this.modal = true
    },
    toggle(item, value) {
      if (value === 'solid') {
        value = 'none'
      } else {
        value = 'solid'
      }
      this.updateElementStyle({
        [item]: value
      })
    },
    // 元素位置移动
    onPositionChange(key, value) {
      this.setElementPosition({
        [key]: Number(value)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.font-desc {
  text-align: center;
  margin-top: 5px;
}
.border-select {
  position: relative;
  width: 200px;
  height: 200px;
  border: 1px solid #000;
  margin: 0 auto;
  .l,
  .r,
  .t,
  .b {
    position: absolute;
    border: 1px solid #000;
  }
  .l:hover,
  .r:hover,
  .t:hover,
  .b:hover {
    background: #037df3;
  }
  .l {
    width: 20px;
    height: 100px;
    top: 50px;
    left: -10px;
  }
  .r {
    width: 20px;
    height: 100px;
    top: 50px;
    right: -10px;
  }
  .t {
    width: 100px;
    height: 20px;
    top: -10px;
    left: 50px;
  }
  .b {
    width: 100px;
    height: 20px;
    bottom: -10px;
    left: 50px;
  }
}
.common-style-panel /deep/ .h-input-number-input {
  padding: 0;
}
/deep/ .h-input-number-append {
  width: 25px;
  padding: 5px 8px;
}
/deep/ .vc-chrome .vc-chrome-toggle-btn {
  display: none;
}
/deep/ .vc-chrome {
  box-shadow: none;
}
</style>

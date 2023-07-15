<template>
  <div class="button-content">
    <collapse-wrap name="按钮类型">
      <h-row>
        <h-radio-group type="button" size="small" v-model="value['button-type']"
          @on-change="setEleprops('button-type', value['button-type'])">
          <h-radio label="normal">
            普通
          </h-radio>
          <h-radio label="suction-bottom" :style="{'margin-left': '8px', 'border-left': '1px solid #d7dde4'}">
            吸底
          </h-radio>
        </h-radio-group>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="按钮背景">
      <h-row>
        <img-select :size="5120" description="支持png、jpg、jpeg、gif格式，图标大小不超过5MB" :accept="['png', 'jpg', 'jpeg', 'gif']"
          v-model="propertyProxy['background-image']" type="background" @input="getImgUrl"></img-select>
      </h-row>
      <h-row>
        <div :style="{'text-align': 'center', 'margin-bottom': '8px'}">按钮图片</div>
      </h-row>
      <div class="color-panel">
        <!-- <el-color-picker size="mini" show-alpha v-model="value['background-color']"
          @change="setEleprops('background-color', $event)">
        </el-color-picker> -->
        <ColorSelect
          padding="5px 7px"
          icon="plus-round"
          emptyHeight="26"
          emptyWidth="26"
          placement="right"
          :isReset="true"
          :selectedColor="propertyProxy['background-color']"
          :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000', '#0030FF']"
          @updateColor="setEleprops('background-color', $event)"
          @resetColor="setEleprops('background-color', 'rgba(255,255,255,1)')"
        />
      </div>
      <h-row>
        <div :style="{'text-align': 'center'}">按钮颜色</div>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="文字样式">
      <h-row>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" v-model="value['font-size']" setzero :min="12"
            @on-change="setEleprops('font-size', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6" style="margin-left: 42px">
          <h-input-number :precision="0" size="small" v-model="value['letter-spacing']" setzero :min="0"
            @on-change="setEleprops('letter-spacing', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6" style="margin-left: 41px">
          <h-input-number :precision="1" size="small" v-model="value['line-height']" setzero :min="1" :step="0.1"
            @on-change="setEleprops('line-height', $event)"></h-input-number>
        </h-col>
      </h-row>
      <h-row>
        <h-col :span="6">
          <p class="font-desc">字号</p>
        </h-col>
        <h-col :span="6" style="margin-left: 42px">
          <p class="font-desc">字间距</p>
        </h-col>
        <h-col :span="6" style="margin-left: 41px">
          <p class="font-desc">行间距</p>
        </h-col>
      </h-row>
      <h-row class="font-panel">
        <h-col :span="6" style="margin-left: 4px">
          <h-radio-group type="button" size="small" v-model="value['font-weight']"
            @on-change="setEleprops('font-weight', value['font-weight'])">
            <h-radio :canCancel="true" label="bold"><span class="iconfont icon-jiacu" title="加粗"></span></h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="value['font-style']"
            @on-change="setEleprops('font-style', value['font-style'])">
            <h-radio :canCancel="true" label="italic">
              <span class="iconfont icon-qingxie-" title="倾斜"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="value['text-decoration']"
            @on-change="setEleprops('text-decoration', value['text-decoration'])">
            <h-radio :canCancel="true" label="underline"><span class="iconfont icon-ziyuan" title="下划线"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="value['text-decoration']"
            @on-change="setEleprops('text-decoration', value['text-decoration'])">
            <h-radio :canCancel="true" label="line-through"><span class="iconfont icon-strikethrough"
                title="删除线"></span></h-radio>
          </h-radio-group>
        </h-col>
        <h-col :span="8" style="margin-left: 38px">
          <ColorSelect
            padding="5px 7px"
            icon="plus-round"
            emptyHeight="26"
            emptyWidth="26"
            placement="top"
            :isReset="true"
            :selectedColor="propertyProxy['color']"
            @updateColor="setEleprops('color', $event)"
            @resetColor="setEleprops('color', 'rgba(0,0,0,1)')"
          />
        </h-col>
      </h-row>
      <h-row>
        <h-col :span="6">
          <p class="font-desc">文本选项</p>
        </h-col>
        <h-col :span="8" style="margin-left: 4px">
          <p class="font-desc">颜色</p>
        </h-col>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="对齐方式">
      <div class="align-panel">
        <h-radio-group type="button" size="small" v-model="value['justify-content']"
          @on-change="setEleprops('justify-content', $event)">
          <h-radio :canCancel="true" label="flex-start">
            <h-tooltip content="左对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-left.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-icon--" title="左对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="center">
            <h-tooltip content="居中对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-center.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-juzhongduiqi" title="居中对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="flex-end">
            <h-tooltip content="右对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-end.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-youduiqi" title="右对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="justify">
            <h-tooltip content="两端对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-justify.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-liangduanduiqi" title="两端对齐"></span> -->
          </h-radio>
        </h-radio-group>
        <h-radio-group type="button" size="small" class="right-align-panel" v-model="value['align-items']"
          @on-change="setEleprops('align-items', $event)">
          <h-radio :canCancel="true" label="flex-start">
            <h-tooltip content="上对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-top.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-dingduiqi-" title="上对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="center">
            <h-tooltip content="垂直居中" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-vertical-center.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-juzhongduiqi1" title="居中对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="flex-end">
             <h-tooltip content="下对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-bottom.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-diduiqi-" title="下对齐"></span> -->
          </h-radio>
        </h-radio-group>
      </div>
    </collapse-wrap>
    <collapse-wrap name="边距">
      <h-row class="edge-panel">
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="value['padding-left']"
            @on-change="setEleprops('padding-left', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="value['padding-right']"
            @on-change="setEleprops('padding-right', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="value['padding-top']"
            @on-change="setEleprops('padding-top', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="value['padding-bottom']"
            @on-change="setEleprops('padding-bottom', $event, true)"></h-input-number>
        </h-col>
      </h-row>
      <h-row>
        <h-col :span="6">
          <p class="font-desc">左侧</p>
        </h-col>
        <h-col :span="6">
          <p class="font-desc">右侧</p>
        </h-col>
        <h-col :span="6">
          <p class="font-desc">顶部</p>
        </h-col>
        <h-col :span="6">
          <p class="font-desc">底部</p>
        </h-col>
      </h-row>
    </collapse-wrap>
  </div>
</template>

<script>
import CollapseWrap from '@Components/collapseWrap'
import ImgSelect from '@Components/ImgSelect'
import ColorSelect from '@Components/ColorSelect'
export default {
  props: [
    'context',
    'selectedElementData'
  ],
  components: {
    CollapseWrap,
    ImgSelect,
    ColorSelect
  },
  data() {
    return {
      value: null,
      topTemp: 0
    }
  },
  computed: {
    propertyProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          if ((name === 'color' || name === 'background-color') && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if ((name === 'color' || name === 'background-color') && !value) {
            value = ''
          }
          let { updateElementProperty } = this.context
          updateElementProperty({ [name]: value })
          return true
        }
      })
    }
  },
  created() {
    this.value = this.selectedElementData.property
    if (typeof this.selectedElementData.style['top'] === 'number') {
      this.topTemp = this.selectedElementData.style['top']
    }
  },
  watch: {
    selectedElementData: {
      handler(val) {
        this.value = val.property
        if (typeof val.style['top'] === 'number') {
          this.topTemp = val.style['top']
        }
      },
      deep: true
    }
  },
  methods: {
    getImgUrl(url) {
      // console.log(url)
      // let { updateElementProperty } = this.context
      // const that = this
      // const image = new Image()
      // image.src = url + '?v=' + Math.random() // 处理缓存
      // image.crossOrigin = '*' // 支持跨域图片
      // image.onload = function () {
      //   const base64 = that.drawBase64Image(image)
      //   updateElementProperty({ 'background-image': base64 })
      // }
      let { updateElementProperty } = this.context
      updateElementProperty({ 'background-image': url })
    },
    // 设置选中元素属性
    setEleprops(key, value, index) {
      // console.log(key, value)
      // 单选
      if (key === 'background-color' && !value) {
        value = ''
      }
      let { updateElementProperty, updateElementStyle } = this.context
      if (key === 'button-type') {
        if (value === 'normal') {
          updateElementStyle({ 'bottom': 'auto', top: this.topTemp, 'position': 'absolute' })
        } else if (value === 'suction-bottom') {
          // updateElementStyle({ 'top': 'auto', 'bottom': 0, 'position': 'fixed' })
          // updateElementStyle({ 'top': 'auto', 'bottom': 0, 'position': 'sticky' })
          updateElementStyle({ 'top': 'auto', 'bottom': 0, 'position': 'fixed' })
        }
      }
      if (Object.prototype.toString.call(key) === '[object Object]') {
        updateElementProperty(key)
      } else {
        updateElementProperty({ [key]: value })
      }
    },
    drawBase64Image (img) {
      const canvas = document.createElement('canvas')
      canvas.width = img.width
      canvas.height = img.height
      const ctx = canvas.getContext('2d')
      ctx.drawImage(img, 0, 0, img.width, img.height)
      const dataURL = canvas.toDataURL('image/png')
      return dataURL
    }
  }
}
</script>

<style scoped lang="scss">
.font-desc {
  text-align: center;
  margin: 0 5px 8px 0;
}
/deep/ .h-radio-group {
  margin-left: -3.5px;
}
.font-panel /deep/ .h-radio-wrapper.h-radio-group-item {
  padding: 2px;
}
.align-panel /deep/ .h-radio-wrapper.h-radio-group-item {
  padding: 0 1px 0 1px;
}
.right-align-panel {
  margin-left: 5px;
}
/deep/ .h-row {
  margin-top: 5px;
}
.edge-panel /deep/ .h-input-number {
  width: 60px;
}
.color-picker-item {
  display: inline-block;
  width: 30px;
  height: 30px;
  border-radius: 2px;
  padding: 1px;
  border: 1px solid #ddd;
  .item {
    width: 26px;
    height: 26px;
    border-radius: 2px;
  }
}
.color-panel {
  position: relative;
  height: 30px;
  width: 100%;
  .color-picker {
    position: absolute;
    left: 2%;
    top: 31%;
  }
  .pre-color {
    // width: 84%;
    position: absolute;
    left: 16%;
    top: 10%;
    // display: flex;
    // justify-content: space-between;
  }
}
.item1 {
  background: #418bf0;
}
.item2 {
  background: #f0b442;
}
.item3 {
  background: #48d93f;
}
.item4 {
  background: #ff0000;
}
.item5 {
  background: #0030ff;
}
.active1 {
  border-color: #418BF0;
}
.active2 {
  border-color: #F0B442;
}
.active3 {
  border-color: #48D93F;
}
.active4 {
  border-color: #FF0000;
}
.active5 {
  border-color: #0030FF;
}
/deep/ .vc-chrome .vc-chrome-toggle-btn {
  display: none;
}
.select-point {
  width: 4px;
  height: 4px;
  border-radius: 2px;
  background: #fff;
  position: relative;
  top: 20px;
  left: 20px;
  display: none;
}
.align-panel-icon{
    width: 20px;
    height: 20px;
  }
</style>

<template>
  <div>
    <collapse-wrap name="文字样式">
      <h-row>
        <h-col :span="6" >
          <h-input-number :precision="0" size="small" v-model="elementProxy['font-size']" setzero :min="12" @on-change="setEleprops('font-size', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6" style="margin-left: 42px">
          <h-input-number :precision="0" size="small" v-model="elementProxy['letter-spacing']" setzero :min="0" @on-change="setEleprops('letter-spacing', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6" style="margin-left: 41px">
          <h-input-number :precision="1" size="small" v-model="elementProxy['line-height']" setzero :min="1" :step="0.1" @on-change="setEleprops('line-height', $event)"></h-input-number>
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
      <h-row class="font-panel"  style="margin-left: 4px">
        <h-col :span="6">
          <h-radio-group type="button" size="small" v-model="elementProxy['font-weight']"  @on-change="setEleprops('font-weight', value['font-weight'])">
            <h-radio :canCancel="true" label="bold"
              ><span class="iconfont icon-jiacu" title="加粗"></span
            ></h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="elementProxy['font-style']" @on-change="setEleprops('font-style', value['font-style'])">
            <h-radio :canCancel="true" label="italic">
              <span class="iconfont icon-qingxie-" title="倾斜"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="elementProxy['text-decoration']" @on-change="setEleprops('text-decoration', value['text-decoration'])">
            <h-radio :canCancel="true" label="underline"
              ><span class="iconfont icon-ziyuan" title="下划线"></span
            ></h-radio>
          </h-radio-group>
          <h-radio-group type="button" size="small" v-model="elementProxy['text-decoration']"  @on-change="setEleprops('text-decoration', value['text-decoration'])">
            <h-radio :canCancel="true" label="line-through"
              ><span class="iconfont icon-strikethrough" title="删除线"></span
            ></h-radio>
          </h-radio-group>
        </h-col>
        <h-col :span="8" style="margin-left: 38px">
          <ColorSelect
            padding="5px 7px"
            icon="plus-round"
            emptyHeight="26"
            emptyWidth="26"
            placement="bottom"
            :isReset="true"
            :selectedColor="elementProxy['color']"
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
        <h-radio-group type="button" size="small" v-model="elementProxy['align-items']" @on-change="setEleprops('align-items', $event)">
          <h-radio :canCancel="true" label="flex-start"
            >
            <h-tooltip content="左对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-left.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-icon--" title="左对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="center"
            >
            <h-tooltip content="居中对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-center.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-juzhongduiqi" title="居中对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="flex-end"
            >
            <h-tooltip content="右对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-end.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-youduiqi" title="右对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="justify"
            >
            <h-tooltip content="两端对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-justify.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-liangduanduiqi" title="两端对齐"></span> -->
          </h-radio>
        </h-radio-group>
        <h-radio-group type="button" size="small" class="right-align-panel" v-model="elementProxy['justify-content']" @on-change="setEleprops('justify-content', $event)">
          <h-radio :canCancel="true" label="flex-start"
            >
            <h-tooltip content="上对齐" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-top.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-dingduiqi-" title="上对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="center"
            >
            <h-tooltip content="垂直居中" placement="top">
              <img class="align-panel-icon" src="@Root/assets/images/align-vertical-center.svg" alt="">
            </h-tooltip>
            <!-- <span class="iconfont icon-juzhongduiqi1" title="居中对齐"></span> -->
          </h-radio>
          <h-radio :canCancel="true" label="flex-end"
            >
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
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="elementProxy['padding-left']" @on-change="setEleprops('padding-left', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="elementProxy['padding-right']" @on-change="setEleprops('padding-right', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="elementProxy['padding-top']" @on-change="setEleprops('padding-top', $event, true)"></h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number :precision="0" size="small" setzero :min="0" v-model="elementProxy['padding-bottom']" @on-change="setEleprops('padding-bottom', $event, true)"></h-input-number>
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
import ColorSelect from '@Components/ColorSelect'
export default {
  name: 'eTextPropsConfig',
  props: [
    'context',
    'selectedElementData'
  ],
  components: {
    CollapseWrap,
    ColorSelect
  },
  data() {
    return {
      value: null
    }
  },
  computed: {
    elementProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          if (name === 'color' && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if (name === 'color' && !value) {
            value = ''
          }
          let { updateElementProperty } = this.context
          if (Object.prototype.toString.call(name) === '[object Object]') {
            updateElementProperty(name)
          } else {
            updateElementProperty({ [name]: value })
          }
          return true
        }
      })
    }
  },
  created () {
    this.value = this.selectedElementData.property
  },
  watch: {
    selectedElementData: {
      handler(val) {
        this.value = val.property
      },
      deep: true
    }
  },
  methods: {
    // 设置选中元素属性
    setEleprops(key, value, type) {
      // 单选
      let { updateElementProperty } = this.context
      if (key == 'color') {
        updateElementProperty({ [key]: value })
      }
      // if (Object.prototype.toString.call(key) === '[object Object]') {
      //   updateElementProperty(key)
      // } else {
      //   if (key == 'align-items') {
      //     switch (value) {
      //       case 'flex-start':
      //         updateElementProperty({ 'text-align': 'left' })
      //         break
      //       case 'center':
      //         updateElementProperty({ 'text-align': 'center' })
      //         break
      //       case 'flex-end':
      //         updateElementProperty({ 'text-align': 'right' })
      //         break
      //       case 'justify':
      //         updateElementProperty({ 'text-align': 'justify' })
      //         break
      //     }
      //   } else {
      //     if (key == 'color') {
      //       await updateElementProperty({ [key]: value })
      //     }
      //   }
      // }
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
  /deep/ .vc-chrome .vc-chrome-toggle-btn {
    display: none;
  }
  .align-panel-icon{
    width: 20px;
    height: 20px;
  }
</style>

<template>
  <div>
    <collapse-wrap name="样式配置">
      <h-row>
        <h-col :span="7" style="line-height:26px;">
          <span class="cell-lable">tab样式</span>
        </h-col>

        <h-col :span="17" class="tab-type">
          <h-radio-group type="button" v-model="propertyProxy['type']">
            <h-radio label="line">
              <img :src="require('@Root/assets/images/tab1@1x.svg')" style="color: #999; font-size: 10px;"
                v-if="propertyProxy['type'] === 'line'">
              <img :src="require('@Root/assets/images/tab1-2@1x.svg')" style="color: #999; font-size: 10px;" v-else>
            </h-radio>
            <h-radio label="card">
              <img :src="require('@Root/assets/images/tab2-2@1x.svg')" style="color: #999; font-size: 10px;"
                v-if="propertyProxy['type'] === 'card'">
              <img :src="require('@Root/assets/images/tab2@1x.svg')" style="color: #999; font-size: 10px;" v-else>
            </h-radio>
          </h-radio-group>
        </h-col>
      </h-row>
      <!-- tab文字默认状态 -->
      <h-row>
        <h-col :span="7" style="line-height:26px;">
          <span>tab文字默认状态</span>
        </h-col>
        <!-- 字号 -->
        <h-col :span="7">
          <h-input-number style="width:87px" :precision="0" size="small"
            v-model="propertyProxy['tab-customer-fontSize']" setzero :min="12" :max="20"></h-input-number>
        </h-col>

        <!-- 文本选项 -->
        <h-col :span="8" class="font-panel">
          <h-radio-group class="font-btn" type="button" size="small" v-model="propertyProxy['tab-customer-fontWeight']">
            <h-radio :canCancel="true" label="bold"><span class="iconfont icon-jiacu" title="加粗"></span></h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small" v-model="propertyProxy['tab-customer-style']">
            <h-radio :canCancel="true" label="italic">
              <span class="iconfont icon-qingxie-" title="倾斜"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small" v-model="propertyProxy['text-customer-direction']">
            <h-radio :canCancel="true" label="underline"><span class="iconfont icon-ziyuan" title="下划线"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small" v-model="propertyProxy['text-customer-direction']">
            <h-radio :canCancel="true" label="line-through"><span class="iconfont icon-strikethrough"
                title="删除线"></span></h-radio>
          </h-radio-group>
        </h-col>

        <!-- 颜色 -->
        <h-col :span="2">
          <ColorSelect padding="5px 7px" icon="plus-round" emptyHeight="26" emptyWidth="26" placement="left-start"
            :isReset="true" :selectedColor="elementProxy['tab-customer-color']"
            @updateColor="setEleprops('tab-customer-color', $event)"
            @resetColor="setEleprops('tab-customer-color', 'rgba(0,0,0,1)')" />
        </h-col>

      </h-row>

      <h-row>
        <h-col :span="17">
          <p class="font-desc">字号</p>
        </h-col>
        <h-col :span="4">
          <p class="font-desc">文本选项</p>
        </h-col>
        <h-col :span="3">
          <p class="font-desc">颜色</p>
        </h-col>
      </h-row>

      <!--  -->
      <h-row>
        <h-col :span="7" style="line-height:26px;">
          <span>tab文字选中状态</span>
        </h-col>
        <!-- 字号 -->
        <h-col :span="7">
          <h-input-number :precision="0" size="small" v-model="propertyProxy['tab-customer-selected-fontSize']" setzero
            :min="12" :max="20">
          </h-input-number>
        </h-col>

        <!-- 文本选项 -->
        <h-col :span="8" class="font-panel">
          <h-radio-group class="font-btn" type="button" size="small"
            v-model="propertyProxy['tab-customer-selected-fontWeight']">
            <h-radio :canCancel="true" label="bold"><span class="iconfont icon-jiacu" title="加粗"></span></h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small"
            v-model="propertyProxy['tab-customer-selected-style']">
            <h-radio :canCancel="true" label="italic">
              <span class="iconfont icon-qingxie-" title="倾斜"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small"
            v-model="propertyProxy['text-customer-selected-direction']">
            <h-radio :canCancel="true" label="underline"><span class="iconfont icon-ziyuan" title="下划线"></span>
            </h-radio>
          </h-radio-group>
          <h-radio-group class="font-btn" type="button" size="small"
            v-model="propertyProxy['text-customer-selected-direction']">
            <h-radio :canCancel="true" label="line-through"><span class="iconfont icon-strikethrough"
                title="删除线"></span></h-radio>
          </h-radio-group>
        </h-col>

        <!-- 颜色 -->
        <h-col :span="2">
          <ColorSelect padding="5px 7px" icon="plus-round" emptyHeight="26" emptyWidth="26" placement="left-start"
            :isReset="true" :selectedColor="elementProxy['tab-selected-color']"
            @updateColor="setEleprops('tab-selected-color', $event)"
            @resetColor="setEleprops('tab-selected-color', 'rgba(0,0,0,1)')" />
        </h-col>
      </h-row>

      <h-row>
        <h-col :span="17">
          <p class="font-desc">字号</p>
        </h-col>
        <h-col :span="4">
          <p class="font-desc">文本选项</p>
        </h-col>
        <h-col :span="3">
          <p class="font-desc">颜色</p>
        </h-col>
      </h-row>

      <!-- tab背景默认状态 -->

      <h-row class="font-panel" style="margin-left: 4px">
        <h-col :span="7" style="line-height:26px;">
          <span>tab背景默认状态</span>
        </h-col>
        <h-col :span="17">
          <div class="color-panel">
            <ColorSelect padding="5px 7px" icon="plus-round" emptyHeight="26" emptyWidth="26" placement="right"
              :isReset="true" :selectedColor="elementProxy['background-color']"
              :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000', '#0030FF']"
              @updateColor="setEleprops('background-color', $event)"
              @resetColor="setEleprops('background-color', 'rgba(255,255,255,1)')" />
          </div>
        </h-col>
      </h-row>

      <!-- tab文字选中状态 -->
      <h-row class="font-panel" style="margin-left: 4px">
        <h-col :span="7" style="line-height:26px;">
          <span>tab背景选中状态</span>
        </h-col>
        <h-col :span="17">
          <div class="color-panel">
            <ColorSelect padding="5px 7px" icon="plus-round" emptyHeight="26" emptyWidth="26" placement="right"
              :isReset="true" :selectedColor="elementProxy['selected-background-color']"
              :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000', '#0030FF']"
              @updateColor="setEleprops('selected-background-color', $event)"
              @resetColor="setEleprops('selected-background-color', 'rgba(255,255,255,1)')" />
          </div>
        </h-col>
      </h-row>

    </collapse-wrap>

    <collapse-wrap name="tab配置">
      <h-row class="font-panel" style="margin-left: 4px">
        <div>
          <h-button type="goast" :disabled="propertyProxy.tabList.length > 9" @click="add">新增</h-button>
        </div>
      </h-row>
      <h-row class="tab-panel" style="margin-left: 4px" v-for="(item, index) in value.tabList" :key="item.id">
        <h-col :span="24">
          <div class="cell-line-container">
            <span style="background-color: #fff;position: relative;padding-right:5px;">tab{{index + 1}}</span>
            <span class="tab-edit_btn" @click="delTab(index)">×</span>
            <span class="tab-edit_btn" @click="sortTab('down', index)">↓</span>
            <span class="tab-edit_btn" @click="sortTab('up', index)">↑</span>
          </div>
        </h-col>
        <h-col :span="7">
          <span class="cell-lable">tab名称</span>
        </h-col>
        <h-col :span="17">
          <h-input v-model="item.title" placeholder="请输入" :maxlength="10" style="margin-bottom: 8px;"
            @on-change="setOptionTabTitle"></h-input>
        </h-col>
        <!--  -->
        <h-col :span="7">
          <span class="cell-lable">选择包含元素</span>
        </h-col>
        <h-col :span="17">
          <h-select multiple v-model="item.elementContent" placeholder="选择目标元素"
            @on-item-remove="(value) => removeItem(value, index)" @on-drop-change="setCurrentSelect(index)">
            <template v-for="i in item.canUseElement">
              <h-option :value="i.uuid" :label="i.element_name || i.name" :key="i.uuid">
                {{i.element_name || i.name}} </h-option>
            </template>
          </h-select>
        </h-col>
      </h-row>
    </collapse-wrap>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import CollapseWrap from '@Components/collapseWrap'
import ColorSelect from '@Components/ColorSelect'
import { generateUID } from '@h5Designer/utils'
import { cloneDeep, difference } from 'lodash'

export default {
  name: 'eTabsPropsConfig',
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
      value: null,
      currentSelectIndex: '',
      activeTime: 0
    }
  },
  computed: {
    ...mapGetters('cms/elements', ['selectedPageElements']),
    // ...mapGetters('cms/editState', ['selectedElementData']),
    elementProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          if (name.indexOf('color') > 0 && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if (name.indexOf('color') > 0 && !value) {
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
    },
    propertyProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          let { updateElementProperty } = this.context
          updateElementProperty({ [name]: value })
          return true
        }
      })
    }
  },
  created() {
    let tempValue = cloneDeep(this.selectedElementData.property)
    let array = this.selectedPageElements
    let map = new Map()
    let temp = []
    for (let i = 0; i < array.length; i++) {
      if (array[i].name !== 'hs-cms-tabs') {
        temp.push(array[i].uuid)
        map.set(array[i].uuid, array[i])
      }
    }

    let tabList = tempValue.tabList
    for (let j = 0; j < tabList.length; j++) {
      let list = tabList[j].elementContent
      let canList = tabList[j].canUseElement
      list.forEach((it, index) => {
        if (temp.indexOf(it) === -1) {
          // 不存在
          tempValue.tabList[j].elementContent.splice(index, 1)
        }
      })
      canList.forEach((it, index) => {
        if (temp.indexOf(it.uuid) !== -1) {
          // 存在
          tempValue.tabList[j].canUseElement[index] = map.get(it.uuid)
        }
      })
    }

    this.value = tempValue
  },
  beforeDestroy() {
    let { updateElementProperty } = this.context
    let obj = {
      ...this.selectedElementData.property,
      uuid: this.selectedElementData.uuid
    }
    obj.tabList = this.value.tabList

    updateElementProperty(obj)
  },
  watch: {
    selectedElementData: {
      handler(val, oldVal) {
        if (val.uuid === oldVal.uuid) {
          if (val.property.tabList !== oldVal.property.tabList) {
            this.value.tabList = cloneDeep(val.property.tabList)
          }
          if (val.property.activeIndex !== oldVal.property.activeIndex) {
            this.value.activeIndex = val.property.activeIndex
          }
        }
      },
      deep: true
    },
    currentSelectIndex: {
      handler(val) {
        if (val === '') {
          this.onMultiChange()
        }
      },
      deep: true
    }
  },
  methods: {
    sortTab(type, index) {
      let arr = cloneDeep(this.value.tabList)
      if (type === 'up') {
        if (index === 0) {
          return false
        }
        let temp = arr[index]
        arr[index] = arr[index - 1]
        arr[index - 1] = temp
        let { updateElementProperty } = this.context
        updateElementProperty({ tabList: arr, activeTime: this.activeTime++ })
      } else {
        if (index === arr.length - 1) {
          return false
        }
        let temp = arr[index]
        arr[index] = arr[index + 1]
        arr[index + 1] = temp
        let { updateElementProperty } = this.context
        updateElementProperty({ tabList: arr, activeTime: this.activeTime++ })
      }
    },
    delTab(index) {
      let arr = cloneDeep(this.value.tabList)
      if (arr.length === 2) {
        this.$hMessage.error('tab组件最少需要保留2个子标签')
        return false
      }
      if (index !== this.value.activeIndex) {
        for (let k = 0; k < arr[index].elementContent.length; k++) {
          document.getElementById(arr[index].elementContent[k]).style.display = 'block'
        }
      }
      arr.splice(index, 1)
      let { updateElementProperty } = this.context
      updateElementProperty({ tabList: arr, activeTime: this.activeTime++ })
    },
    setEleprops(key, value, type) {
      // 单选
      let { updateElementProperty } = this.context
      updateElementProperty({ [key]: value })
    },
    add() {
      let optionIndex = 0
      let arr = cloneDeep(this.value.tabList)
      arr.forEach((item) => {
        if (item.title.indexOf('标签') == 0) {
          let newNameIndex = item.title.substr(2)
          if (Number(newNameIndex) + '' !== NaN + '') {
            if (newNameIndex > optionIndex) {
              optionIndex = parseInt(newNameIndex)
            }
          }
        }
      })
      optionIndex = optionIndex + 1
      const option = {
        'id': generateUID(),
        'title': '标签' + optionIndex,
        'elementContent': [],
        'canUseElement': []
      }
      arr.push(option)
      let { updateElementProperty } = this.context
      updateElementProperty({ tabList: arr })
    },
    setOptionTabTitle() {
      const arr = cloneDeep(this.value.tabList)
      let { updateElementProperty } = this.context
      updateElementProperty({ tabList: arr })
    },
    setCurrentSelect(index) {
      if (this.currentSelectIndex === index) {
        this.currentSelectIndex = ''
        return false
      }
      this.currentSelectIndex = index
      // console.log(index)
      let val = this.selectedPageElements
      let res = []
      let temp = []
      let map = new Map()
      for (let k = 0; k < val.length; k++) {
        if (val[k].name !== 'hs-cms-tabs') {
          temp.push(val[k].uuid)
          map.set(val[k].uuid, val[k])
        }
      }
      let arr = cloneDeep(this.value.tabList)
      for (let i = 0; i < arr.length; i++) {
        if (i !== index) {
          res = difference(temp, arr[i].elementContent)
          temp = res
        }
      }
      let result = []
      for (let j = 0; j < temp.length; j++) {
        result.push(map.get(temp[j]))
      }
      arr[index].canUseElement = result
      let { updateElementProperty } = this.context
      updateElementProperty({ activeIndex: index, tabList: arr, activeTime: this.activeTime++ })
    },
    onMultiChange() {
      let arr = cloneDeep(this.value.tabList)
      // for (let i = 0; i < arr.length; i++) {
      //   if (arr[i].elementContent.length > 5) {
      //     this.$hMessage.info('单个tab页，最多只支持嵌套5个组件')
      //     return false
      //   }
      // }
      let { updateElementProperty } = this.context
      updateElementProperty({ tabList: arr, activeTime: this.activeTime++ })
    },
    removeItem(item, index) {
      if (this.currentSelectIndex === '') {
        this.currentSelectIndex = index
      }
      document.getElementById(item.value).style.display = 'block'
      let { updateElementProperty } = this.context
      updateElementProperty({ activeIndex: index, activeTime: this.activeTime++ })
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
  padding: 0 7px 0 7px;
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
.tab-panel {
  margin: 8px;
}

.font-btn {
  width: 28px;
}

.cell-line-container {
  margin-bottom: 8px;
  &:before {
    content: '';
    position: absolute;
    display: block;
    width: 100%;
    height: 1px;
    background: transparent;
    border-top: 1px dashed #ddd;
    top: 9px;
    left: 0;
  }
  .tab-edit_btn {
    width: 16px;
    height: 16px;
    color: #999999;
    background-color: #fff;
    cursor: pointer;
    float: right;
    position: relative;
    right: 0;
    top: 0;
  }
}
.cell-lable {
  text-align: right;
  width: 92%;
  height: 28px;
  display: block;
  line-height: 28px;
}

.tab-type {
  /deep/ .h-radio-group-button .h-radio-wrapper {
    border: 0px;
    padding: 0px;
  }

  /deep/ .h-radio-wrapper-checked:first-child {
    box-shadow: none !important;
  }

  /deep/ .h-radio-group-button .h-radio-wrapper:first-child {
    border-left: 0px;
    box-shadow: none !important;
  }

  /deep/ .h-radio-wrapper-checked {
    box-shadow: none !important;
  }
}
</style>

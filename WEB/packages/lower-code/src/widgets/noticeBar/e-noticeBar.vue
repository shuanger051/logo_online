<template>
  <div>
    <collapse-wrap name="滚动字幕设置">
      <h-row class="row-panel">
        <h-col :span="7" class="col-panel">
          <p class="font-desc">滚动字幕样式：</p>
        </h-col>
        <h-col :span="6" class="col-panel">
          <h-select
            v-model="propertyProxy['font-size']"
            style="width: 80px; height: 26px; float: right;margin-right: 10px"
            :clearable="false"
            :on-change="setFontSize"
          >
            <h-option
              v-for="(item, index) in fontSizeList"
              :value="item"
              :key="index"
              >{{ item }}
            </h-option>
          </h-select>
        </h-col>
        <h-col :span="9" class="col-panel">
          <FontStyle
            :fontWeight="propertyProxy['font-weight']"
            :fontStyle="propertyProxy['font-style']"
            :textDecoration="propertyProxy['text-decoration']"
            @updateFunc="updateFunc"
          ></FontStyle>
        </h-col>
        <h-col :span="1" class="col-panel" style="margin-left: 1px">
          <ColorSelect
            padding="5px 7px"
            icon="plus-round"
            emptyHeight="20"
            emptyWidth="20"
            placement="left-start"
            :isReset="true"
            :selectedColor="propertyProxy['color']"
            @updateColor="setEleprops('color', $event)"
            @resetColor="setEleprops('color', '#f60')"
          />
        </h-col>
      </h-row>
      <h-row class="row-panel">
        <h-col :span="7" class="col-panel">
          <p class="font-desc">组件背景颜色：</p>
        </h-col>
        <h-col :span="10" class="col-panel">
          <ColorSelect
            padding="5px 7px"
            icon="plus-round"
            emptyHeight="26"
            emptyWidth="26"
            preHeight="23px"
            preWidth="23px"
            placement="bottom"
            :isReset="true"
            :selectedColor="propertyProxy['background-color']"
            :preColorList="['#fff7cc', '#418BF0', '#F0B442', '#48D93F']"
            @updateColor="setEleprops('background-color', $event)"
            @resetColor="setEleprops('background-color', 'rgba(255,255,255,1)')"
          />
        </h-col>
        <h-col :span="6.5" class="col-panel">
          <h-input
            v-model="selectedElementData.property['background-color']"
            @on-change="changeBackgroundColor"
            style="width: 70px; height: 26px; margin-left: 15px;"
            :maxlength="7"
          ></h-input>
        </h-col>
      </h-row>
      <h-row class="row-panel">
        <h-col :span="7" class="col-panel">
          <p class="font-desc">字幕滚动速度：</p>
        </h-col>
        <h-slider
          style="width: 230px; margin-left: 100px; padding-top: 5px;"
          v-model="propertyProxy.speed"
          :tip-format="format"
          :min="1"
          :max="4">
        </h-slider>
      </h-row>
      <h-row class="row-panel-button">
        <h-button type="primary" :disabled="propertyProxy.messageList.length > 9" @click="add">新增滚动字幕</h-button>
        <div style="color: #999; font-weight: 400; margin-left: 190px;">{{propertyProxy.messageList.length}}/10</div>
      </h-row>
      <h-row>
        <draggable v-model="propertyProxy.messageList" @update="dragEnd" :messageList="{animation:200}">
          <div class="radio-content-option" v-for="(item, index) in propertyProxy.messageList" :key="index">
            <img :src="require('@Root/assets/images/drage-dot.svg')" width="14" height="14" style="margin-right: 7px">
            <div>滚动字幕{{item.order_no}}：</div>
            <h-input
              v-model="propertyProxy.messageList[index].op_desc"
              placeholder="请输入"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 3 }"
              style="width: 223px;"
              :maxlength="100"
              @on-change="setOptionDesc()"
            ></h-input>
            <span class="radio-content-option-icon">
              <img v-if="propertyProxy.messageList.length > 1" :src="require('@Root/assets/images/minus.svg')" width="16" height="16" @click="minus(index)">
            </span>
          </div>
        </draggable>
      </h-row>
    </collapse-wrap>
  </div>
</template>
<script>
import CollapseWrap from '@Components/collapseWrap'
import ColorSelect from '@Components/ColorSelect'
import draggable from 'vuedraggable'
import { generateUID } from '@h5Designer/utils'
import FontStyle from '@Components/FontStyle'
import { cloneDeep } from 'lodash'

export default {
  name: 'eNoticeBarPropsConfig',
  props: [
    'context',
    'selectedElementData'
  ],
  components: {
    CollapseWrap,
    ColorSelect,
    FontStyle,
    draggable
  },
  data() {
    return {
      fontSizeList: [12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72],
      value: null,
      messageIndex: 1,
      property: this.selectedElementData.property
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
  watch: {
    selectedElementData: {
      handler(val) {
        this.value = val.property
      },
      deep: true
    }
  },
  created() {
    this.value = this.selectedElementData.property
  },
  methods: {
    // 设置选中元素属性
    setEleprops(key, value, type) {
      console.log('key===>', key, 'value===>', value)
      // 单选
      if (key === 'background-color' && !value) {
        value = ''
      }
      let { updateElementProperty } = this.context
      if (Object.prototype.toString.call(key) === '[object Object]') {
        updateElementProperty(key)
      } else {
        updateElementProperty({ [key]: value })
      }
    },

    // 更新字体样式
    updateFunc(data) {
      console.log('data===>', data)
      const key = data.name
      this.setEleprops(key, data.value, true)
    },

    // 字幕滚动速度
    format(val) {
      if (val === 1) {
        return '慢'
      } else if (val === 2) {
        return '普通'
      } else if (val === 3) {
        return '较快'
      } else {
        return '快'
      }
    },

    // 组件背景颜色变换
    changeBackgroundColor() {
      if (this.selectedElementData.property['background-color'].indexOf('#') == 0 && this.selectedElementData.property['background-color'].length == 7) {
        let { updateElementProperty } = this.context
        updateElementProperty({ 'background-color': this.selectedElementData.property['background-color'] })
      }
    },

    // 拖动改变顺序
    dragEnd(evt) {
      evt.preventDefault()
    },

    // 跑马灯内容设置
    setOptionDesc() {
      const arr = cloneDeep(this.propertyProxy.messageList)
      let { updateElementProperty } = this.context
      updateElementProperty({ messageList: arr })
    },

    // 最下方插入一个选项
    add() {
      let optionIndex = 0
      let arr = cloneDeep(this.propertyProxy.messageList)
      arr.forEach((item) => {
        if (item.op_desc.indexOf('滚动') == 0) {
          let newNameIndex = item.op_desc.substr(7)
          if (Number(newNameIndex) + '' !== NaN + '') {
            if (newNameIndex > optionIndex) {
              optionIndex = parseInt(newNameIndex)
            }
          }
        }
      })
      optionIndex = optionIndex + 1
      this.messageIndex = optionIndex
      const option = {
        'op_id': generateUID(),
        'op_desc': '滚动跑马灯消息' + optionIndex,
        // 'order_no': arr.length + 1
        'order_no': optionIndex
      }
      arr.push(option)
      let { updateElementProperty } = this.context
      updateElementProperty({ messageList: arr })
    },

    // 去除当前选项
    minus(index) {
      const arr = cloneDeep(this.propertyProxy.messageList)
      arr.splice(index, 1)
      let { updateElementProperty } = this.context
      updateElementProperty({ messageList: arr })
    }
  }
}
</script>
<style scoped lang="scss">
  .row-panel-button {
    margin-top: 15px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
  .col-panel {
    height: 45px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .radio-content-option {
    display: flex;
    align-items: center;
    margin-top:8px;
  }
  .radio-content-option-icon {
    width: 12px;
    display: flex;
    flex-direction: column;
    margin-left: 7px;
  }
</style>

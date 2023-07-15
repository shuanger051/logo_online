<template>
  <div>
    <collapse-wrap :name="groupName" v-if="list.length" :hideBar="true">
      <!-- <div class="group-name">
      <span>{{ groupName }}</span>
    </div> -->
      <h-row class="">
        <h-col v-for="item in list" :span="8" :key="item.id">
          <components-btn
            :handleClick="handleClick.bind(this, item.component_name, item.component_id, item.component_show_name, item.component_type)"
            :name="item.component_show_name" :componentName="item.component_name" :componentId="item.component_id"
            :componentIcon="item.component_icon" :groupType="groupType" :componentType="item.component_type">
          </components-btn>
        </h-col>
      </h-row>
    </collapse-wrap>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import componentsBtn from './componentsBtn'
import { getEditRuntimeContext } from '@h5Designer/core/runtime_context'
import { cloneDeep } from 'lodash'
import { generateUID, addCheckElement } from '@h5Designer/utils'
import CollapseWrap from '@Components/collapseWrap'
// import { getWidgetIdFunc, getWidgetNameFunc } from '@Utils/utils'

export default {
  name: 'componentsGroup',
  components: {
    componentsBtn,
    CollapseWrap
  },
  computed: {
    ...mapGetters('cms/elements', ['selectedPageElements']),
    ...mapState('cms/elements', [
      'items'
    ])
  },
  props: {
    groupName: {
      type: String,
      default: () => ''
    },
    groupType: {
      type: String,
      default: () => ''
    },
    list: {
      type: Array,
      default: () => []
    },
    hideBar: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
    }
  },
  mixins:[addCheckElement],
  methods: {
    async handleClick(name, id, showName, type) {
      let runtimeContext = getEditRuntimeContext()
      let componentConfig = runtimeContext.configGetter.getConfig(name)
      // let flag = false
      // let hasTabs = false
      // let index = 0
      // let maxHeight = 0
      // 前置判断，一个作品只能存在一个表单提交按钮组件
      // let keys = Object.keys(this.items)
      // if (keys.length) {
      //   keys.forEach((key) => {
      //     this.items[key].forEach((item) => {
      //       if (item.name === 'hs-cms-form-submit') {
      //         flag = true
      //       }
      //     })
      //   })
      // }
      // this.selectedPageElements.forEach((item) => {
        // let eachHeight = item.style.top + item.style.height
        // if (eachHeight > maxHeight) {
        //   maxHeight = eachHeight
        // }
        // if (item.name == 'hs-cms-form-submit') {
        //   flag = true
        // }
      //   if (item.name == 'hs-cms-tabs') {
      //     hasTabs = true
      //   }
      //   if (item.element_name) {
      //     if (item.element_name.indexOf(showName) == 0) {
      //       let newNameIndex = item['element_name'].substr(showName.length)
      //       if (Number(newNameIndex) + '' !== NaN + '') {
      //         if (item.name == name) {
      //           if (newNameIndex > index) {
      //             index = parseInt(newNameIndex)
      //           }
      //         }
      //       }
      //     }
      //   }
      // })
      // index = index + 1
      // if (name == 'hs-cms-tabs' && hasTabs) {
      //   this.$hMessage.error('一个页面只允许添加一个tab组件')
      //   return false
      // }
      // if (name == 'hs-cms-form-submit' && flag) {
      //   this.$hMessage.error('一个作品只允许添加一个表单提交按钮')
      //   return false
      // }
      let newComponent = {
        uuid: generateUID(),
        // element_name: showName,
        extra: {
          initStatus: 1 // 1显示 0隐藏
        },
        componentId: id,
        componentType: type,
        name,
        style: {
          ...componentConfig.defaultStyle
          // top: maxHeight
        },
        property: {
          ...componentConfig.defaultProperty
        },
        editPanelConfig: {
          hideEvents: false,
          ...componentConfig.editPanelConfig
        },
        extraRenderPanel: componentConfig.extraRenderPanel || []
      }
      let newComponentObj = cloneDeep(newComponent)
      switch (newComponentObj.name) {
        case 'hs-cms-carousel':
          newComponentObj.property.imgList[0].uuid = generateUID()
          break
        case 'hs-cms-form-radio':
          newComponentObj.property.qu_id = generateUID()
          newComponentObj.property.options.forEach(item => {
            item['op_id'] = generateUID()
          })
          break
        case 'hs-cms-form-checkbox':
          newComponentObj.property.qu_id = generateUID()
          newComponentObj.property.options.forEach(item => {
            item['op_id'] = generateUID()
          })
          break
        case 'hs-cms-form-completion':
          newComponentObj.property.qu_id = generateUID()
          break
        case 'hs-cms-form-shortAnswerQuestion':
          newComponentObj.property.qu_id = generateUID()
          break
        case 'hs-cms-form-smsVerifiCode':
          // eslint-disable-next-line no-case-declarations
          let smsFormUid = newComponentObj.property.smsFormUid
          Object.keys(smsFormUid).forEach(key=>{
            smsFormUid[key] = generateUID()
          })
          newComponentObj.property.qu_id = generateUID()
        break
      }
      await this.addCheckElement(this.selectedPageElements, this.items, name, showName, newComponentObj)
      // -------------------创建widget_id开始-------------------------------
      // const property = newComponentObj.property || {}
      // if (property['main-widget'] == 'true' && !property['widget-id']) {
      //   getWidgetIdFunc().then(async (id) => {
      //     newComponentObj.property['widget-id'] = id
      //     const name = getWidgetNameFunc(property['act-rule']['widget_base_config']['widget_name'])
      //     newComponentObj.property['act-rule']['widget_base_config']['widget_name'] = name
      //     await this.$store.dispatch('cms/elements/addElement', newComponentObj)
      //   })
      // } else {
      //   await this.$store.dispatch('cms/elements/addElement', newComponentObj)
      // }
    }
  }
}
</script>
<style lang="scss" scoped>
.group-name {
  position: relative;
  margin: 15px 0 10px;
  span {
    position: relative;
    display: inline-block;
    font-size: 12px;
    line-height: 14px;
    color: #333;
    padding: 0 6px;
    background-color: #fff;
  }
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
}
</style>

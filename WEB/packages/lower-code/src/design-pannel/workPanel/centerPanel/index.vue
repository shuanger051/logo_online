<template>
  <div @click="handleClick" @drop="ondrop" @dragover="allowDrop" @mousedown="bindMultipleEvent">
    <div style="width: 260px;margin: 0 auto; margin-top: 20px; display: flex; align-items: center">
      <div @click="prePage()"
        style="width: 22px; height: 22px; background: #fff; border-radius: 11px; text-align: center;line-height: 22px; margin-right: 10px">
        <h-icon name="chevron-left" style="color: #999; font-size: 10px;"></h-icon>
      </div>
      <h-select :value="selectedPage" :key="this.pageName" style="width: 200px;" :clearable="false"
        @on-change="selectPage">
        <h-option v-for="item in pages" :label="item.name" :value="item.uuid" :key="item.uuid">{{ item.name }}
        </h-option>
      </h-select>
      <div @click="nextPage()"
        style="width: 22px; height: 22px; background: #fff; border-radius: 11px; text-align: center;line-height: 22px; margin-left: 10px">
        <h-icon name="chevron-right" style="color: #999; font-size: 10px; margin-left: 2px"></h-icon>
      </div>
    </div>
    <div :style="scaleFun">
      <ruler-line :horizontalArr="horizontalArr" :verticalArr="verticalArr"></ruler-line>
      <e-edit-panel :horizontalArr="horizontalArr" :verticalArr="verticalArr"></e-edit-panel>
      <adjust-height v-if="!castratedVersion" @adjust-work-height="adjustHeightLine"></adjust-height>
    </div>
  </div>
</template>
<script>

import { mapState, mapActions, mapGetters } from 'vuex'
import eEditPanel from '@h5Designer/components/e-edit-panel'
import RulerLine from './rulerLine.vue'
import AdjustHeight from './adjustHeight.vue'

import { findIndex, cloneDeep } from 'lodash'
import { generateUID, addCheckElement } from '@h5Designer/utils'
import { getEditRuntimeContext } from '@h5Designer/core/runtime_context'
// import { getWidgetIdFunc, getWidgetNameFunc } from '@Utils/utils'
export default {
  name: 'CenterPanel',
  components: { eEditPanel, RulerLine, AdjustHeight },
  props: ['worksContent'],
  computed: {
    ...mapState('cms/editState', [
      'scaleRate', 'selectedElement', 'selectedList'
    ]),
    ...mapState('cms/elements', [
      'items'
    ]),
    // ...mapState('cms/works', [
    //   'width', 'height'
    // ]),
    ...mapState('cms/pages', {
      pages: state => state.items
    }),
    ...mapState('cms/editState', {
      selectedPage: state => state.selectedPage
    }),
    ...mapGetters('cms/elements', ['selectedPageElements']),
    horizontalArr: function () {
      const index = this.pages.findIndex(item => { return item.uuid == this.selectedPage })
      if (index !== -1) {
        return this.calculateRulerArr(50, this.pages[index].style.width)
      }
    },
    verticalArr: function () {
      const index = this.pages.findIndex(item => { return item.uuid == this.selectedPage })
      if (index !== -1) {
        return this.calculateRulerArr(50, this.pages[index].style.height)
      }
    },
    scaleFun: function () {
      const index = this.pages.findIndex(item => { return item.uuid == this.selectedPage })
      var scale, width, height
      if (index !== -1) {
        scale = this.scaleRate
        width = this.pages[index].style.width + 'px'
        height = this.pages[index].style.height + 50 + 'px'
      }
      // var halfHeight = -this.height / 2 + 'px'
      // transform-origin: center top;
      return `transform:scale(${scale}); transform-origin: center top; position: relative; width:${width}; height:${height}; margin:40px auto;`
    }
  },
  data() {
    return {
      pageName: '',
      multipleSelect: false,
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true' // 是否打开Lite版
    }
  },
  mixins:[addCheckElement],
  watch: {
    pages: {
      handler(value) {
        this.$forceUpdate()
        let selectedPage = this.pages.find(item => { return item.uuid == this.selectedPage })
        if (selectedPage) {
          this.pageName = selectedPage.name
        }
      },
      deep: true
    }
  },
  methods: {
    ...mapActions('cms/editState', [
      'updateEditState',
      'multiSelectElement'
    ]),
    handleClick(e) {
      // console.log('点击空白')
      function containsEshape(node) {
        if (node === null) {
          return true
        } else if (node.classList.contains('u-edit-shape')) {
          return true
        } else if (node.classList.contains('center-panel')) {
          return false
        } else if (node.classList.contains('editmenu-content')) {
          return true
        } else {
          return containsEshape(node.parentNode)
        }
      }
      if (this.selectedElement || this.selectedList.length > 0) {
        // console.log('清除当前选中和多选数据')
        if (!containsEshape(e.target)) {
          if (e.ctrlKey) return
          this.updateEditState({ selectedElement: null, selectedList: [] })
        }
        }
    },
    calculateRulerArr(space, value) {
      let arr = [0]
      let i = 0
      while (value >= space) {
        i++
        arr.push(i * space)
        value -= space
      }
      return arr
    },

    async ondrop(e) {
      let { clientX, clientY } = e
      let editpanel = document.getElementsByClassName('edit-panel')[0]
      e.preventDefault()
      let component_name = e.dataTransfer.getData('component_name')
      // let flag = false
      // let hasTabs = false
      let component_show_name = e.dataTransfer.getData('component_show_name')
      let component_id = e.dataTransfer.getData('component_id')
      let component_type = e.dataTransfer.getData('component_type')
      let runtimeContext = getEditRuntimeContext()
      let componentConfig = runtimeContext.configGetter.getConfig(component_name)
      if (!componentConfig) return
      let newComponent = {
          uuid: generateUID(),
          // element_name: component_show_name,
          extra: {
            initStatus: 1 // 1显示 0
          },
          name: component_name,
          componentId: component_id,
          componentType: component_type,
          style: {
            ...componentConfig.defaultStyle,
            top: componentConfig.defaultStyle.height == 'auto' ? clientY - editpanel.getBoundingClientRect().top : clientY - (componentConfig.defaultStyle.height / 2) - editpanel.getBoundingClientRect().top,
            left: (componentConfig.editDefaultAbility && componentConfig.editDefaultAbility && componentConfig.editDefaultAbility.direction === 'vertical') ? componentConfig.defaultStyle.left : (clientX - (componentConfig.defaultStyle.width / 2) - editpanel.getBoundingClientRect().left)
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
        if (component_name === 'hs-cms-image') {
          newComponent.property.top = componentConfig.defaultStyle.height == 'auto' ? clientY - editpanel.getBoundingClientRect().top : clientY - (componentConfig.defaultStyle.height / 2) - editpanel.getBoundingClientRect().top
          newComponent.property.left = (componentConfig.editDefaultAbility && componentConfig.editDefaultAbility && componentConfig.editDefaultAbility.direction === 'vertical') ? componentConfig.defaultStyle.left : (clientX - (componentConfig.defaultStyle.width / 2) - editpanel.getBoundingClientRect().left)
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
      let res = await this.addCheckElement(this.selectedPageElements, this.items, component_name, component_show_name, newComponentObj)
      if(!res) return
      console.log('success')
      // let index = 0
      // 前置判断，一个作品只能存在一个表单提交按钮组件
      // let keys = Object.keys(this.items)
      // console.log(keys)
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
      //   // if (item.name == 'hs-cms-form-submit') {
      //   //   flag = true
      //   // }
      //   if (item.name == 'hs-cms-tabs') {
      //     hasTabs = true
      //   }
      //   if (item.element_name) {
      //     if (item.element_name.indexOf(component_show_name) == 0) {
      //       let newNameIndex = item['element_name'].substr(component_show_name.length)
      //       if (Number(newNameIndex) + '' !== NaN + '') {
      //         if (item.name == component_name) {
      //           if (newNameIndex > index) {
      //             index = parseInt(newNameIndex)
      //           }
      //         }
      //       }
      //     }
      //   }
      // })
      // index = index + 1
      // if (component_name == 'hs-cms-tabs' && hasTabs) {
      //   this.$hMessage.error('一个页面只允许添加一个tab组件')
      //   return false
      // }
      // if (component_name == 'hs-cms-form-submit' && flag) {
      //   this.$hMessage.error('一个作品只允许添加一个表单提交按钮')
      //   return false
      // }
      // if (!componentConfig) {
      //   return
      // }
      // let newComponent = {
      //   uuid: generateUID(),
      //   element_name: component_show_name + index,
      //   extra: {
      //     initStatus: 1 // 1显示 0
      //   },
      //   name: component_name,
      //   componentId: component_id,
      //   componentType: component_type,
      //   style: {
      //     ...componentConfig.defaultStyle,
      //     top: componentConfig.defaultStyle.height == 'auto' ? clientY - editpanel.getBoundingClientRect().top : clientY - (componentConfig.defaultStyle.height / 2) - editpanel.getBoundingClientRect().top,
      //     left: (componentConfig.editDefaultAbility && componentConfig.editDefaultAbility && componentConfig.editDefaultAbility.direction === 'vertical') ? componentConfig.defaultStyle.left : (clientX - (componentConfig.defaultStyle.width / 2) - editpanel.getBoundingClientRect().left)
      //   },
      //   property: {
      //     ...componentConfig.defaultProperty
      //   },
      //   editPanelConfig: {
      //     hideEvents: false,
      //     ...componentConfig.editPanelConfig
      //   },
      //   extraRenderPanel: componentConfig.extraRenderPanel || []
      // }
      // if (component_name === 'hs-cms-image') {
      //   newComponent.property.top = componentConfig.defaultStyle.height == 'auto' ? clientY - editpanel.getBoundingClientRect().top : clientY - (componentConfig.defaultStyle.height / 2) - editpanel.getBoundingClientRect().top
      //   newComponent.property.left = (componentConfig.editDefaultAbility && componentConfig.editDefaultAbility && componentConfig.editDefaultAbility.direction === 'vertical') ? componentConfig.defaultStyle.left : (clientX - (componentConfig.defaultStyle.width / 2) - editpanel.getBoundingClientRect().left)
      // }
      // let newComponentObj = cloneDeep(newComponent)
      // switch (newComponentObj.name) {
      //   case 'hs-cms-carousel':
      //     newComponentObj.property.imgList[0].uuid = generateUID()
      //     break
      //   case 'hs-cms-form-radio':
      //     newComponentObj.property.qu_id = generateUID()
      //     newComponentObj.property.options.forEach(item => {
      //       item['op_id'] = generateUID()
      //     })
      //     break
      //   case 'hs-cms-form-checkbox':
      //     newComponentObj.property.qu_id = generateUID()
      //     newComponentObj.property.options.forEach(item => {
      //       item['op_id'] = generateUID()
      //     })
      //     break
      //   case 'hs-cms-form-completion':
      //     newComponentObj.property.qu_id = generateUID()
      //     break
      // }
      // // -------------------创建widget_id开始-------------------------------
      // const property = newComponentObj.property || {}
      // if (property['main-widget'] == 'true' && !property['widget-id']) {
      //   await getWidgetIdFunc().then((id) => {
      //     newComponentObj.property['widget-id'] = id
      //     const name = getWidgetNameFunc(property['act-rule']['widget_base_config']['widget_name'])
      //     newComponentObj.property['act-rule']['widget_base_config']['widget_name'] = name
      //     this.$store.dispatch('cms/elements/addElement', newComponentObj)
      //   })
      // } else {
      //   await this.$store.dispatch('cms/elements/addElement', newComponentObj)
      // }
      // ---
      let selectIndex = findIndex(this.pages, { uuid: this.selectedPage })
      let oldHeight = this.pages[selectIndex]['style'].height
      const element = document.getElementById(this.selectedElement)
      console.log('this.$store.state.cms.elements.items[this.selectedPage]',this.$store.state.cms.elements.items[this.selectedPage])
      let elementName = this.$store.state.cms.elements.items[this.selectedPage].find(item => { return item.uuid == this.selectedElement }).name
      if (clientY - editpanel.getBoundingClientRect().top + element.getBoundingClientRect().height > oldHeight) {
        let newHeight = clientY - editpanel.getBoundingClientRect().top + element.getBoundingClientRect().height - oldHeight + 10
        switch (elementName) {
          case 'hs-cms-business-indexperformance':
            newHeight = newHeight + 60
            break
          case 'hs-cms-business-tradeperformance':
            newHeight = newHeight + 75
            break
          case 'hs-cms-business-conceptperformance':
            newHeight = newHeight + 70
            break
          case 'hs-cms-business-marketturnovers':
            newHeight = newHeight + 45
            break
          case 'hs-cms-business-hotstock':
            newHeight = newHeight + 30
            break
          case 'hs-cms-business-fundrecommend':
            newHeight = newHeight + 30
            break
          case 'hs-cms-business-fundsflow':
            newHeight = newHeight + 100
            break
          case 'hs-cms-business-morrowstock':
            newHeight = newHeight + 290
            break
        }
        this.adjustHeightLine(newHeight)
      }
    },
    allowDrop(e) {
      e.preventDefault()
    },
    async adjustHeightLine(height) {
      let index = findIndex(this.pages, { uuid: this.selectedPage })
      let oldHeight = this.pages[index]['style'].height
      let newHeight = oldHeight + height
      if (oldHeight + height > 10000) {
        newHeight = 10000
        return false
        // this.$hMessage.info('页面高度最高为10000')
      } else if (oldHeight + height < 812) {
        newHeight = 812
        return false
      }
      await this.$store.dispatch('cms/pages/updatePages', Object.assign({ selectedPage: this.pages[index].uuid }, {
        height: newHeight
      }))
      var scrollTarget = document.getElementsByClassName('center-panel')[0]
      scrollTarget.scrollTop = newHeight
    },
    selectPage(value) {
      let newState = {
        currentState: 'edit',
        selectedPage: value,
        selectedElement: null,
        ignore: true
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
    },
    prePage() {
      console.log('上一页')
      const { cms } = this.$store.state
      const pages = cms.pages.items
      let index = pages.findIndex((item) => { return item.uuid == this.selectedPage })
      if (index == 0) {
        return
      }
      this.$store.dispatch('cms/editState/updateEditState', { selectedPage: pages[index - 1].uuid, ignore: true })
    },
    nextPage() {
      const { cms } = this.$store.state
      const pages = cms.pages.items
      let index = pages.findIndex((item) => { return item.uuid == this.selectedPage })
      if (index == pages.length - 1) {
        return
      }
      this.$store.dispatch('cms/editState/updateEditState', { selectedPage: pages[index + 1].uuid, ignore: true })
    },
    bindMultipleEvent(event) {
      if (event.buttons !== 1 || event.which !== 1) return
      if (!event.ctrlKey) return
      let _this = this
      let selectedEls = []
      let mouseOn = false
      let startX = 0
      let startY = 0
      let canvas = document.getElementsByClassName('center-panel')[0]
      // if (this.selectedElement) return
      this.updateEditState({ selectedElement: null, selectedList: [] })
      document.onmousedown = function (e) {
        selectedEls = []
        clearEventBubble(e)
        // console.log('multiMouseDown')
        mouseOn = true
        startX = e.clientX
        startY = e.clientY
        let selDiv = document.createElement('div')
        selDiv.style.cssText = 'position:absolute;width:0;height:0;margin:0;padding:0;border:1px dashed #eee;background-color:#007aff;z-index:1000;opacity:0.3;display:none;'
        selDiv.id = 'selectDiv'
        document.body.appendChild(selDiv)
        selDiv.style.left = startX + 'px'
        selDiv.style.top = startY + 'px'
      }

      document.onmousemove = function (e) {
        if (!mouseOn) return
        // console.log('multiMouseMove')
        clearEventBubble(e)
        let _x = e.clientX
        let _y = e.clientY
        let selDiv = document.getElementById('selectDiv')
        let react_sel = selDiv.getClientRects()[0]
        selDiv.style.display = 'block'
        selDiv.style.left = Math.min(_x, startX) + 'px'
        selDiv.style.top = Math.min(_y, startY) + 'px'
        selDiv.style.width = Math.abs(_x - startX) + 'px'
        selDiv.style.height = Math.abs(_y - startY) + 'px'
        if (!react_sel) return
        let fileDivs = canvas.getElementsByClassName('u-edit-shape')
        // console.log(fileDivs)
        // 获取参数
        for (let i = 0; i < fileDivs.length; i++) {
          if (fileDivs[i].style.display === 'none') {
            continue
          }
          let react = fileDivs[i].getClientRects()[0]
          // console.log(fileDivs[i], react)
          if (collide(react, react_sel) === true) {
            // 该DOM元素被选中，进行处理
            if (selectedEls.indexOf(fileDivs[i].id) === -1) {
              selectedEls.push(fileDivs[i].id)
            }
          } else {
            if (selectedEls.indexOf(fileDivs[i].id) !== -1) {
              selectedEls.splice(i, 1)
            }
          }
        }
      }

      // 添加鼠标松开事件监听
      document.onmouseup = function (e) {
        // console.log('multiMouseUp')
        clearEventBubble(e)
        // 恢复参数
        let selDiv = document.getElementById('selectDiv')
        selDiv.style.display = 'none'

        mouseOn = false
        document.onmousedown = null
        document.onmousemove = null
        document.onmouseup = null
        if (!e.ctrlKey) return
        if (_this.selectedList.length > 0 || selectedEls.length > 0) {
          _this.$store.dispatch('cms/elements/multiSelectElement', selectedEls)
        }
      }

      function clearEventBubble(e) {
        if (e.stopPropagation) e.stopPropagation()
        else e.cancelBubble = true

        if (e.preventDefault) e.preventDefault()
        else e.returnValue = false
      }

      function collide(rect1, rect2) {
        // console.log(rect1, rect2)
        const maxX = Math.max(rect1.x + rect1.width, rect2.x + rect2.width)
        const maxY = Math.max(
          rect1.y + rect1.height,
          rect2.y + rect2.height
        )
        const minX = Math.min(rect1.x, rect2.x)
        const minY = Math.min(rect1.y, rect2.y)
        if (
          maxX - minX <= rect1.width + rect2.width &&
          maxY - minY <= rect1.height + rect2.height
        ) {
          return true
        } else {
          return false
        }
      }
    }
  }
}

</script>
<style lang="scss" scoped>
</style>

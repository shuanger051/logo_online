import { mapState, mapActions, mapGetters } from 'vuex'
import eEditShape from '../model/e-edit-shape'
import { toleranceMaker, resolveEditShapeStyle, resolveEditElementStyle } from '../utils'
import { getEditRuntimeContext } from '@h5Designer/core/runtime_context'
import { partial, findIndex, cloneDeep, keys } from 'lodash'
import { generateUID, addCheckElement } from '@h5Designer/utils'
import hotkeys from 'hotkeys-js'
import EditMenu from '../support/editMenu'
import MultiSelectMenu from '../support/multiSelectMenu'
import { setCurrentWidgetIdList, getWidgetIdFunc, getWidgetNameFunc } from '@Utils/utils'
import TextMenu from '../support/textMenu'
import { find } from 'lodash'

const tolerance = toleranceMaker()
const RegLeft = /l/
const RegTop = /t/

export default {
  name: 'eEditPanel',
  props: [
    'horizontalArr',
    'verticalArr',
    'pannelStyle'
  ],
  data() {
    return {
      vLine: [],
      hLine: [],
      oldStyles: {},
      oldStyle: {},
      editmenuPos: [],
      textMenuUuid: '', // 点击文本对象属性
      textMenuShow: false, // 文本菜单显示隐藏
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true' // 是否打开Lite版
    }
  },
  watch: {
    'selectedPageElements.length': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          window.sessionStorage.setItem('currentWidgetList', '[]')
          const lsit = []
          Object.keys(this.getElementItems).forEach((key) => {
            lsit.push(...this.getElementItems[key])
          })
          // 更新当前作品的widget
          lsit.map(item => {
            if (item.property['main-widget'] === 'true') {
              setCurrentWidgetIdList(item.property)
            }
          })
        }
      },
      deep: true
    },
    selectedElement(val) {
      console.log(val)
      if (!val) {
        this.$store.dispatch('cms/works/clearRef')
      }
      this.hideEditMenu()
      this.hideTextMenu(val)
    }
  },
  components: { eEditShape, EditMenu, TextMenu, MultiSelectMenu},
  mixins: [addCheckElement],
  computed: {
    ...mapGetters('cms/elements', [
      'selectedPageElements',
      'getElementItems'
    ]),
    // ...mapState('cms/works', [
    //   'width',
    //   'height'
    // ]),
    ...mapState('cms/pages', [
      'items'
    ]),
    ...mapState('cms/elements', {
      eleItems: state => state.items
    }),
    ...mapState('cms/editState', [
      'selectedElement',
      'selectedList',
      'currentState',
      'selectedPage',
      'clipboard',
      'editLoading'
    ]),
    ...mapGetters('cms/editState', [
      'selectedElementData'
    ])
  },
  methods: {
    ...mapActions('cms/editState', [
      'updateEditState'
    ]),
    ...mapActions('cms/elements', [
      'updateElementStyle',
      'multiUpdateElementStyle',
      'multiUpdateElementProperty',
      'updateElementProperty',
      'deleteElement',
      'multiDeleteElement',
      'copyToClipboard'
    ]),
    hideEditMenu () {
      this.editmenuPos = []
    },
    hideTextMenu(uuid) {
      if (this.textMenuUuid !== uuid) {
        this.textMenuShow = false
        this.textMenuUuid = cloneDeep(uuid)
      }
    },
    elementControl(params) {
      switch (params.type) {
        case 'copy':
          this.elementCopy()
          break
        case 'delete':
          this.elementDelete()
          break
        case 'paste':
          this.elementPaste()
          break
      }
    },
    bindContextMenu (e) {
      this.$store.dispatch('cms/elements/multiSelectElement', [])
      const { x, y } = this.$el.getBoundingClientRect()
      this.editmenuPos = [e.clientX - x, e.clientY - y]
    },
    startMoveHandler(element) {
      // console.log('startMove')
      this.oldStyles = {}
      // console.log(element)
      if (!element.property || (element.property && element.property.cantMove)) {
        return
      }
      if (element.name === 'hs-cms-text') {
        this.hideTextMenu(element.uuid)
      }
      if (this.selectedList.length > 1 && this.selectedList.indexOf(element.uuid) !== -1) {
        if (this.selectedElement !== element.uuid) {
          this.updateEditState({
            selectedElement: element.uuid
          })
        }
        this.oldStyle = {
          ...element.style
        }
        let array = []
        for (let i = 0; i < this.selectedPageElements.length; i++) {
          if (this.selectedList.indexOf(this.selectedPageElements[i].uuid) !== -1) {
            array.push(this.selectedPageElements[i])
          }
        }
        for (let j = 0; j < array.length; j++) {
          this.oldStyles[array[j].uuid] = array[j]
        }
      } else {
        if (this.selectedList.length > 0) {
          this.$store.dispatch('cms/elements/multiSelectElement', [])
        }
        if (this.selectedElement !== element.uuid) {
          this.updateEditState({
            selectedElement: element.uuid
          })
        }
        this.oldStyle = {
          ...element.style
        }
      }
    },
    async pointMoveHandler(direction, pos) {
      const elementName = this.selectedPageElements.find(item => { return item.uuid == this.selectedElement }).name
      let { width, height, left, top } = this.oldStyle
      let {dx, dy} = pos
      let isLeft = RegLeft.test(direction)
      let isTop = RegTop.test(direction)
      let style
      if (parseFloat(top).toString() === 'NaN') {
        style = {
          width: width + (isLeft ? 0 - dx : dx),
          height: height + (isTop ? 0 - dy : dy),
          left: isLeft ? left + dx : left,
          top: top
        }
      } else {
        style = {
          width: width + (isLeft ? 0 - dx : dx),
          height: height + (isTop ? 0 - dy : dy),
          left: isLeft ? left + dx : left,
          top: isTop ? top + dy : top
        }
        const autoScal = this.selectedElementData.editPanelConfig.autoScal //1:1缩放
        const minWidth = this.selectedElementData.editPanelConfig.minWidth
        const minHeight = this.selectedElementData.editPanelConfig.minHeight
        let minSize = minWidth||minHeight
        if (direction == 'lt') {
          if (elementName == 'hs-cms-image' || elementName == 'hs-cms-video' || elementName == 'hs-cms-carousel' || elementName == 'hs-cms-qrCode'|| elementName == 'hs-cms-button') {
            let scale = height / width
            dy = scale * dx
            if (autoScal&&minSize && (width - dx) < minSize||(height - dy)<minSize) {
              style = {
                width: minSize,
                height: minSize,
                left: left,
                top: top
              }
            } else {
              style = {
                width: width - dx,
                height: height - dy,
                left: left + dx,
                top: top + dy
              }
            } 
          }
        }
        if (direction == 'lb') {
          if (elementName == 'hs-cms-image' || elementName == 'hs-cms-video' || elementName == 'hs-cms-carousel'||elementName == 'hs-cms-qrCode'|| elementName == 'hs-cms-button') {
            let scale = height / width
            dy = scale * dx
            if (autoScal&&minSize && (width - dx) < minSize || (height - dy) < minSize) {
              style = {
                width: minSize,
                height: minSize,
                left: left,
                top: top
              }
            } else {
              style = {
                width: width - dx,
                height: height - dy,
                left: left + dx,
                top: top
              }
            }
          }
        }
        if (direction == 'rt') {
          if (elementName == 'hs-cms-image' || elementName == 'hs-cms-video' || elementName == 'hs-cms-carousel' || elementName == 'hs-cms-qrCode'|| elementName == 'hs-cms-button') {
            let scale = height / width
            dy = scale * dx
            if (autoScal && minSize && (width + dx) < minSize || (height + dy) < minSize) {
              style = {
                width: minSize,
                height: minSize,
                left: left,
                top: top
              }
            } else {
              style = {
                width: width + dx,
                height: height + dy,
                left: left,
                top: top - dy
              }
            }
          }
        }
        if (direction == 'rb') {
          if (elementName == 'hs-cms-image' || elementName == 'hs-cms-video' || elementName == 'hs-cms-carousel' || elementName == 'hs-cms-qrCode'|| elementName == 'hs-cms-button') {
            let scale = height / width
            dy = scale * dx
            if (autoScal && minSize && (width + dx) < minSize || (height + dy) < minSize) {
              style = {
                width: minSize,
                height: minSize,
                left: left,
                top: top
              }
            } else {
              style = {
                width: width + dx,
                height: height + dy,
                left: left,
                top: top
              }
            }
          }
        }
      }
      // if (elementName == 'hs-cms-image') {
      //   await this.updateElementProperty(style)
      // } else {
      //   await this.updateElementStyle(style)
      // }
      await this.updateElementStyle(style)

      let newHeight = top + dy + height
      let selectIndex = findIndex(this.items, { uuid: this.selectedPage })
      let oldHeight = this.items[selectIndex]['style'].height
      if (direction.indexOf('t') > -1) return

      if (newHeight > oldHeight) {
        if (newHeight > 10000) {
          newHeight = 10000
          return false
          // this.$hMessage.info('页面高度最高为10000')
        } else if (newHeight < 812) {
          newHeight = 812
          return false
        }
        await this.$store.dispatch('cms/pages/updatePages', Object.assign({ selectedPage: this.selectedPage }, {
          height: newHeight + 10
        }))
        oldHeight = newHeight
        // var scrollTarget = document.getElementsByClassName('center-panel')[0]
        // scrollTarget.scrollTop = newHeight
      }
    },
    pointMoveUpHandler(pos) {
    },
    moveUpHandler(element, {dx, dy}) {
      // console.log(element)
      // console.log(`fdx：${dx} - fdy：${dy}`)
      if (dx === 0 && dy === 0) {
        this.$store.dispatch('cms/elements/multiSelectElement', [])
        this.clearLine()
        return false
      }
      // const element = this.selectedPageElements.find(item => { return item.uuid == this.selectedElement })
      if (!element) {
        return
      }
      if (!element.property || (element.property && element.property.cantMove)) {
        return
      }
      if (this.selectedList.length > 1 && this.selectedList.indexOf(element.uuid) !== -1) {
        // let propertyMap = {}
        let styleMap = {}
        for (const key in this.oldStyles) {
          const elementTmp = this.selectedPageElements.find(item => { return item.uuid == key })
          if (!elementTmp) {
            continue
          }
          if (!elementTmp.property || (elementTmp.property && elementTmp.property.cantMove)) {
            continue
          }
          if (parseFloat(this.oldStyles[key].style.top).toString() === 'NaN') {
            if (elementTmp.editPanelConfig && elementTmp.editPanelConfig.positionSet && elementTmp.editPanelConfig.positionSet.left && elementTmp.editPanelConfig.positionSet.left.disabled) {
              styleMap[this.oldStyles[key].uuid] = {
                top: this.oldStyles[key].style.top,
                left: this.oldStyles[key].style.left
              }
            } else {
              styleMap[this.oldStyles[key].uuid] = {
                top: this.oldStyles[key].style.top,
                left: this.oldStyles[key].style.left + dx
              }
            }
          } else {
            // if (this.oldStyles[key].name == 'hs-cms-image') {
            //   propertyMap[this.oldStyles[key].uuid] = {
            //     left: this.oldStyles[key].style.left + dx,
            //     top: this.oldStyles[key].style.top + dy
            //   }
            // } else {
            if (elementTmp.editPanelConfig && elementTmp.editPanelConfig.positionSet && elementTmp.editPanelConfig.positionSet.left && elementTmp.editPanelConfig.positionSet.left.disabled) {
              styleMap[this.oldStyles[key].uuid] = {
                top: this.oldStyles[key].style.top + dy,
                left: this.oldStyles[key].style.left
              }
            } else {
              styleMap[this.oldStyles[key].uuid] = {
                top: this.oldStyles[key].style.top + dy,
                left: this.oldStyles[key].style.left + dx
              }
            }
            // }
          }
        }
        if (keys(styleMap).length > 0) {
          this.multiUpdateElementStyle(styleMap)
        }
        // if (keys(propertyMap).length > 0) {
        //   this.multiUpdateElementProperty(propertyMap)
        // }
        this.oldStyles = {}
      } else {
        if (!element) {
          return
        }
        if (!element.property || (element.property && element.property.cantMove)) {
          return
        }
        if (parseFloat(this.oldStyle.top).toString() === 'NaN') {
          let curElement = document.getElementById(element.uuid)
          this.updateElementStyle({
            left: parseInt(curElement.style.left),
            top: parseInt(curElement.style.top)
          })
        } else {
          let selectIndex = findIndex(this.items, { uuid: this.selectedPage })
          let oldHeight = this.items[selectIndex]['style'].height
          let curElement = document.getElementById(element.uuid)
          // if (element.name == 'hs-cms-image') {
          //   this.updateElementProperty({
          //     left: this.oldStyle.left + dx,
          //     top: this.oldStyle.top + dy
          //   })
          // } else {
          //   this.updateElementStyle({
          //     left: this.oldStyle.left + dx,
          //     top: this.oldStyle.top + dy
          //   })
          // }
          this.updateElementStyle({
            left: parseInt(curElement.style.left),
            top: parseInt(curElement.style.top)
          })
          if ((parseInt(curElement.style.top) + this.oldStyle.height) > oldHeight) {
            let newHeight = oldHeight + parseInt(curElement.style.top) + this.oldStyle.height - oldHeight + 10
            if (newHeight > 10000) {
              newHeight = 10000
              return false
              // this.$hMessage.info('页面高度最高为10000')
            }
            this.$store.dispatch('cms/pages/updatePages', Object.assign({ selectedPage: this.selectedPage }, {
              height: newHeight
            }))
            let scrollTarget = document.getElementsByClassName('center-panel')[0]
            scrollTarget.scrollTop = newHeight
          }
        }
        this.clearLine()
      }
    },
    moveHandler({dx, dy}) {
      const element = this.selectedPageElements.find(item => { return item.uuid == this.selectedElement })
      if (this.selectedList.length > 1 && this.selectedList.indexOf(element.uuid) !== -1) {
        for (let i = 0; i < this.selectedList.length; i++) {
          const elementTmp = this.selectedPageElements.find(item => { return item.uuid == this.selectedList[i] })
          if (!elementTmp) {
            continue
          }
          if (!elementTmp.property || (elementTmp.property && elementTmp.property.cantMove)) {
            continue
          }
          let curElement = document.getElementById(this.selectedList[i])
          let curId = this.selectedList[i]
          if (parseFloat(this.oldStyles[curId].style.top).toString() === 'NaN') {
            curElement.style.top = this.oldStyles[curId].style.top + 'px'
          } else {
            curElement.style.top = this.oldStyles[curId].style.top + dy + 'px'
          }
          if (elementTmp.editPanelConfig && elementTmp.editPanelConfig.positionSet && elementTmp.editPanelConfig.positionSet.left && elementTmp.editPanelConfig.positionSet.left.disabled) {
            curElement.style.left = this.oldStyles[curId].style.left + 'px'
          } else {
            curElement.style.left = this.oldStyles[curId].style.left + dx + 'px'
          }
        }
      } else {
        if (!element) {
          return
        }
        if (!element.property || (element.property && element.property.cantMove)) {
          return
        }
        this.clearLine()
        let vHandler = this.createToleranceHandler(
          dx, 'left', 'width',
          [...this.verticalArr], []
        )
        let hHandler = this.createToleranceHandler(
          dy, 'top', 'height',
          [...this.horizontalArr], []
        )

        vHandler((dValue, arr) => {
          if (dValue) { dx += dValue }
          this.vLine = arr
        })
        hHandler((dValue, arr) => {
          if (dValue) { dy += dValue }
          this.hLine = arr
        })

        let curElement = document.getElementById(element.uuid)

        curElement.style.left = this.oldStyle.left + dx + 'px'
        curElement.style.top  = this.oldStyle.top + dy + 'px'
        // console.log(`left：${curElement.style.left} - top：${curElement.style.top}`)
      }
    },
    keyDownHandler(e) {
      const element = this.selectedPageElements.find(item => { return item.uuid == this.selectedElement })
      if (!element) {
        return
      }
      if (!element.property || (element.property && element.property.cantMove)) {
        return
      }
      const key = e.keyCode || e.charCode
      if ((key === 8 || key === 46) && !this.castratedVersion) {
        this.elementDelete()
      }
      if (e.preventDefault) {
        e.preventDefault()
      } else {
        e.returnValue = false
      }
      if (key === 37) {
        if (element.componentType === '3') {
          return false
        }
        // if (element.name == 'hs-cms-image') {
        //   this.updateElementProperty({
        //     left: element.style.left - 1
        //   })
        // } else {
        //   this.updateElementStyle({
        //     left: element.style.left - 1
        //   })
        // }
        this.updateElementStyle({
          left: element.style.left - 1
        })
      }
      if (key === 38) {
        // if (element.name == 'hs-cms-image') {
        //   this.updateElementProperty({
        //     top: element.style.top - 1
        //   })
        // } else {
        //   this.updateElementStyle({
        //     top: element.style.top - 1
        //   })
        // }
        this.updateElementStyle({
          top: element.style.top - 1
        })
      }
      if (key === 39) {
        if (element.componentType == '3') {
          return false
        }
        // if (element.name == 'hs-cms-image') {
        //   this.updateElementProperty({
        //     left: element.style.left + 1
        //   })
        // } else {
        //   this.updateElementStyle({
        //     left: element.style.left + 1
        //   })
        // }
        this.updateElementStyle({
          left: element.style.left + 1
        })
      }
      if (key === 40) {
        let selectIndex = findIndex(this.items, { uuid: this.selectedPage })
        let oldHeight = this.items[selectIndex]['style'].height
        // if (element.name == 'hs-cms-image') {
        //   this.updateElementProperty({
        //     top: element.style.top + 1
        //   })
        // } else {
        //   this.updateElementStyle({
        //     top: element.style.top + 1
        //   })
        // }
        this.updateElementStyle({
          top: element.style.top + 1
        })
        this.oldStyle = {
          ...element.style
        }
        if ((this.oldStyle.top + 1 + this.oldStyle.height) > oldHeight) {
          let newHeight = oldHeight + this.oldStyle.top + 1 + this.oldStyle.height - oldHeight + 10
          if (newHeight > 10000) {
            newHeight = 10000
            return false
            // this.$hMessage.info('页面高度最高为10000')
          }
          this.$store.dispatch('cms/pages/updatePages', Object.assign({ selectedPage: this.selectedPage }, {
            height: newHeight
          }))
          let scrollTarget = document.getElementsByClassName('center-panel')[0]
          scrollTarget.scrollTop = newHeight
        }
      }
    },
    createToleranceHandler(dValue, leftOrTop, widthOrHeight, target, source) {
      let {selectedPageElements, oldStyle} = this

      selectedPageElements.forEach(element => {
        let {style} = element
        if (element.uuid === this.selectedElement) {
          return
        }
        target.push(
          style[leftOrTop],
          (style[widthOrHeight] / 2) + style[leftOrTop],
          style[leftOrTop] + style[widthOrHeight]
        )
      })
      source.push(
        oldStyle[leftOrTop] + dValue,
        (oldStyle[widthOrHeight] / 2) + oldStyle[leftOrTop] + dValue,
        oldStyle[leftOrTop] + oldStyle[widthOrHeight] + dValue
      )

      return tolerance.bind(null, target, source)
    },
    clearLine() {
      // 判断是否为空，防止多次刷新
      if (this.vLine.length || this.hLine.length) {
        this.vLine = []
        this.hLine = []
      }
    },
    elementCopy() {
      let BLACKLIST = [{
        name: 'hs-cms-formExt-result',
        label: '表单结果组件'
      }, {
        name: 'hs-cms-form-submit',
        label: '表单提交组件'
      }]
      if (!this.selectedElement) return
      const element = this.selectedPageElements.filter(item => item.uuid === this.selectedElement)
      let res = find(BLACKLIST, { name: element[0].name })
      if (res) {
        this.$hMessage.error(`${res.label}无法复制`)
        return
      }
      this.$store.dispatch('cms/elements/copyToClipboard', element)
    },
    elementPaste() {
      if (!this.clipboard.length) {
        return false
      }
      let target = cloneDeep(this.clipboard[0])
      console.log(this.clipboard[0], '源')
      target.uuid = generateUID()
      // let flag = false
      // let hasTabs = false
      // let index = 0
      let showName = target.element_name.replace(/[0-9]+/g, '')
      // target.element_name = showName + index
      // if (index === 1) {
      //   target.style.top = 0
      //   target.style.left = 0
      // } else {
      //   target.style.top = target.style.top + 20
      //   if (target.editPanelConfig && target.editPanelConfig.positionSet && target.editPanelConfig.positionSet.left && target.editPanelConfig.positionSet.left.disabled) {
      //     target.style.left = target.style.left
      //   } else {
      //     target.style.left = target.style.left + 20
      //   }
      // }
      switch (target.name) {
        case 'hs-cms-carousel':
          target.property.imgList[0].uuid = generateUID()
          break
        case 'hs-cms-form-radio':
          target.property.qu_id = generateUID()
          // target.property.options.forEach(item => {
          //   item['op_id'] = generateUID()
          // })
          break
        case 'hs-cms-form-checkbox':
          target.property.qu_id = generateUID()
          // target.property.options.forEach(item => {
          //   item['op_id'] = generateUID()
          // })
          break
        case 'hs-cms-form-completion':
          target.property.qu_id = generateUID()
          break
        case 'hs-cms-form-shortAnswerQuestion':
          target.property.qu_id = generateUID()
          break
        case 'hs-cms-form-smsVerifiCode':
          target.property.qu_id = generateUID()
          break
        case 'hs-cms-tabs':
          let tempTabList = target.property.tabList
          for (let l = 0; l < tempTabList.length; l++) {
            tempTabList[l].elementContent = []
            tempTabList[l].canUseElement = []
          }
          break
      }
      console.log(target, 'last-target')

      this.addCheckElement(this.selectedPageElements, this.eleItems, target.name, showName, target, 'paste')
      // 前置判断，一个作品只能存在一个表单提交按钮组件
      // let keys = Object.keys(this.eleItems)
      // console.log(keys)
      // if (keys.length) {
      //   keys.forEach((key) => {
      //     this.eleItems[key].forEach((item) => {
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
      //     if (item.element_name.indexOf(showName) == 0) {
      //       let newNameIndex = item['element_name'].substr(showName.length)
      //       if (Number(newNameIndex) + '' !== NaN + '') {
      //         if (item.name == target.name) {
      //           if (newNameIndex > index) {
      //             index = parseInt(newNameIndex)
      //           }
      //         }
      //       }
      //     }
      //   }
      // })
      // index = index + 1
      // if (target.name == 'hs-cms-tabs' && hasTabs) {
      //   this.$hMessage.error('一个页面只允许添加一个tab组件')
      //   return false
      // }
      // if (target.name == 'hs-cms-form-submit' && flag) {
      //   this.$hMessage.error('一个作品只允许添加一个表单提交按钮')
      //   return false
      // }

      // 活动组件添加widget-id
      // if (target.property['main-widget'] == 'true' && !target.property['widget-id']) {
      //   getWidgetIdFunc().then((id) => {
      //     target.property['widget-id'] = id
      //     const name = getWidgetNameFunc(target.property['act-rule']['widget_base_config']['widget_name'])
      //     target.property['act-rule']['widget_base_config']['widget_name'] = name
      //     this.$store.dispatch('cms/elements/addElement', target)
      //   })
      // } else {
      //   this.$store.dispatch('cms/elements/addElement', target).then(() => {
      //     // this.$store.dispatch('cms/elements/copyToClipboard', target)
      //   })
      // }
    },
    elementDelete() {
      let curElement = this.selectedElement
      if (this.selectedElementData.name === 'hs-cms-form-submit' && this.selectedElementData.property.showSubmitResult === '1') {
        this.$hMsgBox.confirm({
          title: '提示',
          content: `继续删除将删除表单提交按钮与对应表单结果页，是否继续？`,
          okText: `继续删除`,
          cancelText: `取消`,
          onOk: async () => {
            // 删除表单提交组件
            this.deleteElement(this.selectedElement)
            // 清除事件
            if (this.$store.state.cms.events.items[this.selectedPage]) {
              let findItem = this.$store.state.cms.events.items[this.selectedPage].find(item => { return item.trigger.target === this.selectedElement })
              if (findItem) {
                let uuidArr = [findItem.uuid]
                this.$store.dispatch('cms/events/deleteEvents', uuidArr)
              }
            }
            // 删除新增的页面以及组件
            let { cms } = this.$store.state
            let pages = cms.pages.items
            let page = pages.find((item) => item.property.type === 'formResultPage')
            let index = pages.findIndex((item) => item.property.type === 'formResultPage')
            let elements = cms.elements.items[page.uuid]
            if (elements && elements.length > 0) {
              let newState = {
                selectedElement: null,
                ignore: true
              }
              if (page.uuid == cms.editState.selectedPage) {
                newState.selectedPage = pages[0].uuid
              }
              await this.$store.dispatch('cms/pages/deletePage', {index, ignore: true})
              await this.$store.dispatch('cms/elements/deleteElementsInpage', {uuid: page.uuid, ignore: true})
              await this.$store.dispatch('cms/events/deleteEventsInpage', {uuid: page.uuid, ignore: true})
              this.$store.dispatch('cms/editState/updateEditState', newState)
            } else {
              let newState = {
                selectedElement: null,
                ignore: true
              }
              if (page.uuid == cms.editState.selectedPage) {
                newState.selectedPage = pages[0].uuid
              }
              await this.$store.dispatch('cms/pages/deletePage', {index, ignore: true})
              this.$store.dispatch('cms/editState/updateEditState', newState)
              this.$store.$$init()
            }
          },
          onCancel: () => {
          }
        })
      } else if (this.selectedElementData.name === 'hs-cms-formExt-result') {
        this.$hMsgBox.error({
          title: '警告',
          content: '<p>表单结果组件无法直接删除</p>'
        })
      } else {
        this.deleteElement(curElement)
        this.$store.dispatch('cms/works/clearPassValidate', { clearType: 'clearEleVlidate', pageId: this.selectedPage, elementId: curElement })
        // 清除事件
        if (this.$store.state.cms.events.items[this.selectedPage]) {
          let findItem = this.$store.state.cms.events.items[this.selectedPage].find(item => { return item.trigger.target === curElement })
          if (findItem) {
            let uuidArr = [findItem.uuid]
            this.$store.dispatch('cms/events/deleteEvents', uuidArr)
          }
        }
      }
    },
    multiElementDelete() {
      if (this.selectedList.length > 0) {
        const element = this.selectedPageElements.find(item => { return item.name == 'hs-cms-form-submit' })
        if (element) {
          if (this.selectedList.indexOf(element.uuid) !== -1) {
            if (element.property.showSubmitResult === '1') {
              this.$hMessage.error('存在绑定结果页的表单组件，请先单独删除')
              return false
            }
          }
        }
        this.multiDeleteElement()
        if (this.$store.state.cms.events.items[this.selectedPage]) {
          let findItems = this.$store.state.cms.events.items[this.selectedPage].find(item => { return this.selectedList.indexOf(item.trigger.target) !== -1 })
          if (findItems.length > 0) {
            let uuidArr = findItems.forEach(item => item.uuid)
            this.$store.dispatch('cms/events/deleteEvents', uuidArr)
          }
        }
      }
    },
    intHotKeys() {
      if (hotkeys) {
        hotkeys.unbind()
        // return false
      }
      let hotkeysMap = [
        {
          i18nTooltip: '复制(ctrl+c)',
          action: function () { this.elementCopy() },
          hotkey: 'ctrl&c,⌘&c',
          hotkeyTooltip: '(ctrl+c)'
        },
        {
          i18nTooltip: '粘贴(ctrl+v)',
          action: function () { this.elementPaste() },
          hotkey: 'ctrl&v,⌘&v',
          hotkeyTooltip: '(ctrl+v)'
        },
        {
          i18nTooltip: '删除(del,backspace)',
          action: function () { this.multiElementDelete() },
          hotkey: 'del,backspace',
          hotkeyTooltip: '(del,backspace)'
        }
      ]
      hotkeysMap.map(key => {
        key.hotkey && hotkeys(key.hotkey, { splitKey: '&' }, (event, handler) => {
          event.preventDefault()
          event.stopPropagation()
          key.action && key.action.call(this)
        })
      })
    }
  },
  mounted () {
    console.log('mounted')
    this.intHotKeys()
  },
  activated() {
    console.log('activated')
    this.intHotKeys()
  },
  deactivated() {
    console.log('deactivated')
    hotkeys.unbind()
  },
  destroyed() {
    console.log('destroyed')
    hotkeys.unbind()
  },
  render (h) {
    let index = findIndex(this.items, { uuid: this.selectedPage })
    index = index === -1 ? 0 : index
    let background_image = this.items[index]['style']['background_image']
    let stylePage = {
      width: this.items[index].style.width + 'px',
      height: this.items[index].style.height + 'px'
    }
    let backgroundStyle = Object.assign({}, stylePage)
    if (background_image && background_image !== 'none') {
      backgroundStyle = Object.assign({}, stylePage, { 'background-image': `url(${background_image})`, 'background-repeat': 'no-repeat',
      'background-size': '100% auto', 'background-color': this.items[index]['style']['background_color']})
    } else {
      stylePage['background-color'] = this.items[index]['style']['background_color']
    }
    return (
      <div
        class= { this.currentState === 'preview' ? 'preview-panel' : 'edit-panel'}
        style={stylePage}
        onContextmenu={e => {
          if (!this.clipboard.length) return
          this.bindContextMenu(e)
        }}
        onClick={() => {
          this.hideEditMenu()
          // this.hideTextMenu()
        }}
      >
        <div
          style={backgroundStyle}
        >
        {
          this.selectedPageElements.map((element) => {
            let cantMove = false
            let {uuid, name, style, property, extra} = element
            if (element.property && element.property.cantMove) {
              cantMove = true
            }
            let runtimeContext = getEditRuntimeContext()
            let mode = runtimeContext.mode
            let Ctr = runtimeContext.getComponent(uuid, name)
            let { direction, scalePoint } = runtimeContext.configGetter.getConfig(name).editDefaultAbility
            // 是否打开Lite版
            if (this.castratedVersion) {
              direction = 'forbidden'
              scalePoint = []
            }
            let active = uuid === this.selectedElement
            let selected = this.selectedList.indexOf(uuid) !== -1
            // if (name == 'hs-cms-image') {
            //   style.width = property.width
            //   style.height = property.height
            //   style.left = property.left
            //   style.top = property.top
            // }
            return (
              <e-edit-shape
                tabIndex="0"
                onPointStartMoveHandler = {partial(this.startMoveHandler, element)}
                onPointMoveHandler = {this.pointMoveHandler}
                onPointMoveUpHandler = {this.pointMoveUpHandler}
                onStartMoveHandler = {partial(this.startMoveHandler, element)}
                onMoveUpHandler = {partial(this.moveUpHandler, element)}
                onMoveHandler = {this.moveHandler}
                nativeOnKeydown = {this.keyDownHandler}
                nativeOnContextmenu={e => {
                  this.bindContextMenu(e)
                }}
                active = {active}
                selected = {selected}
                key = {uuid}
                direction = {direction}
                scalePoint = {scalePoint}
                style = {resolveEditShapeStyle(style)}
                id={uuid}
                cantMove={cantMove}
                onDblclickFunc={(e) => {
                  const context = {
                    ...mode.getCurrentModeApi()
                  }
                  this.$store.dispatch('cms/elements/multiSelectElement', [])
                  if (name === 'hs-cms-text' && context.mode == 'edit') {
                    e.preventDefault()
                    // e.stopPropagation()
                    this.textMenuUuid = cloneDeep(this.selectedElement)
                    this.textMenuShow = active
                  }
                }}
                onClickFunc={(e) => {
                  const context = {
                    ...mode.getCurrentModeApi()
                  }
                  if (name === 'hs-cms-text' && context.mode == 'edit') {
                    // e.stopPropagation()
                    this.hideTextMenu(uuid)
                  }
                }}
              >
                {
                  h(Ctr, {
                    props: {
                      style,
                      property,
                      selectedElement: this.selectedElement,
                      context: {
                        ...mode.getCurrentModeApi(),
                        updateElementProperty: this.updateElementProperty,
                        updateElementStyle: this.updateElementStyle
                      },
                      uuid,
                      active
                    },
                    style: resolveEditElementStyle(style)
                  })
                }
                {
                  active && extra.initStatus === 0 ?
                    <h-icon class="browse-colse icon-browse-colse"></h-icon> :
                    null
                }
              </e-edit-shape>
            )
          })
        }
        {
          this.vLine.map((item) => {
            return (<div class="u-line__v" style={{left: item + 'px'}}></div>)
          })
        }
        {
          this.hLine.map((item) => {
            return (<div class="u-line__h" style={{top: item + 'px'}}></div>)
          })
        }
        {
          this.editmenuPos.length
            ? <EditMenu
              style={{
                left: this.editmenuPos[0] + 'px',
                top: this.editmenuPos[1] + 'px',
                userSelect: 'none',
                position: 'absolute',
                zIndex: 999
              }}
              onSelect={({ selectedKeys }) => {
                this.elementControl({ type: selectedKeys })
                this.hideEditMenu()
              }}
            /> : null
        }
        {
          this.selectedList.length > 1 ? <MultiSelectMenu/> : null
        }
        {
          this.selectedPageElements.map((element) => {
            let {uuid, name, style, property} = element
            const active = this.textMenuUuid === uuid

            return (
              this.textMenuShow && active && name === 'hs-cms-text' ?
                <TextMenu
                  style={{
                    left: style.left + 'px',
                    top: (style.top - 40) + 'px',
                    userSelect: 'none',
                    position: 'absolute',
                    zIndex: 999
                  }}
                  uuid = {uuid}
                  property = { property }
                  onUpdateText = {(data) => {
                    if (data) {
                      this.updateElementProperty({ [data.name]: data.value, uuid: data.uuid })
                      this.hideTextMenu(this.selectedElement)
                    }
                  }}
                ></TextMenu> : null
            )
          })
        }
        </div>
      </div>
    )
  }
}

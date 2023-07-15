
import { getWidgetIdFunc, getWidgetNameFunc } from '@Utils/utils'
export let addCheckElement = {
  methods: {
    addCheckElement(selectedPageElements, items, component_name, component_show_name, newComponentObj, action=''){
      // let { clientX, clientY } = e
      let flag = false
      let hasTabs = false
      let hasSms = false
      let fundListComNum = 0
      let index = 0
      let keys = Object.keys(items)
      console.log(keys)
      if (keys.length) {
        keys.forEach((key) => {
          items[key].forEach((item) => {
            if (item.name === 'hs-cms-form-submit') {
              flag = true
            }
            if (item.name === 'hs-cms-form-smsVerifiCode') {
              hasSms = true
            }
            if (item.name == 'hs-cms-business-fundList') {
              fundListComNum += 1
            }
          })
        })
      }
      selectedPageElements.forEach((item) => {
        // if (item.name == 'hs-cms-form-submit') {
        //   flag = true
        // }
        if (item.name == 'hs-cms-tabs') {
          hasTabs = true
        }
        // if (item.name == 'hs-cms-form-smsVerifiCode') {
        //   hasSms = true
        // }
        // if (item.name == 'hs-cms-business-fundList') {
        //   fundListComNum += 1
        // }
        if (item.element_name) {
          if (item.element_name.indexOf(component_show_name) == 0) {
            let newNameIndex = item['element_name'].substr(component_show_name.length)
            if (Number(newNameIndex) + '' !== NaN + '') {
              if (item.name == component_name) {
                if (newNameIndex > index) {
                  index = parseInt(newNameIndex)
                }
              }
            }
          }
        }
      })
      index = index + 1
      return new Promise(async reslove => {
        if (component_name == 'hs-cms-tabs' && hasTabs) {
          this.$hMessage.error('一个页面只允许添加一个tab组件')
          reslove(false)
          return false
        }
        if (component_name == 'hs-cms-form-smsVerifiCode' && hasSms) {
          this.$hMessage.error('一个作品只允许添加一个短信验证码')
          reslove(false)
          return false
        }
        if (component_name == 'hs-cms-business-fundList' && fundListComNum >= 5) {
          this.$hMessage.error('一个作品最多添加5个产品列表组件')
          reslove(false)
          return false
        }
        if (component_name == 'hs-cms-form-submit' && flag) {
          this.$hMessage.error('一个作品只允许添加一个表单提交按钮')
          reslove(false)
          return false
        }
        if (component_name == 'hs-cms-formExt-result') {
          this.$hMessage.error('表单结果组件无法复制')
          reslove(false)
          return false
        }
        // if (action === "paste"&&(component_name === 'hs-cms-getCoupons' || component_name === 'hs-cms-getDongBeiCoupons')) {
        //   this.$hMessage.error('领券组件无法复制')
        //   return false
        // }
        newComponentObj.element_name = component_show_name + index
        if(action === "paste"){
          if (index === 1) {
            newComponentObj.style.top = 0
          } else {
            newComponentObj.style.top = newComponentObj.style.top + 20
          }
          if (newComponentObj.editPanelConfig && newComponentObj.editPanelConfig.positionSet && newComponentObj.editPanelConfig.positionSet.left && newComponentObj.editPanelConfig.positionSet.left.disabled) {
            newComponentObj.style.left = newComponentObj.style.left
          } else {
            newComponentObj.style.left = newComponentObj.style.left + 20
          }
        }
        
        // -------------------创建widget_id开始-------------------------------
        const property = newComponentObj.property || {}
        if (property['main-widget'] == 'true' && !property['widget-id']) {
          await getWidgetIdFunc().then((id) => {
            newComponentObj.property['widget-id'] = id
            const name = getWidgetNameFunc(property['act-rule']['widget_base_config']['widget_name'])
            newComponentObj.property['act-rule']['widget_base_config']['widget_name'] = name
            this.$store.dispatch('cms/elements/addElement', newComponentObj)
          })
          reslove(true)
        } else {
          await this.$store.dispatch('cms/elements/addElement', newComponentObj)
          reslove(true)
        }
      })
    }
  }
}
import { find, omit, keys } from 'lodash'
import hMessage from 'h_ui/dist/lib/Message'
/**
 * 无模板表单验证
 * @param type 类型：作品级，页面级，组件级
 * @param data
 * @param data.pageId
 * @param data.elementId
 * @param data.vlidateList
 * @return
 */
export function workFormVlidate(type, store, data) {
  let result = true
  // console.log(data)
  let pageItems = store.state.cms.pages.items
  let elItems = store.state.cms.elements.items
  let pageData = find(pageItems, { uuid: data.pageId })
  let eleData = find(elItems[data.pageId], { uuid: data.elementId })
  // console.log(pageData, eleData)
  // 计算错误个数
  let errlength = 0
  let index = 0
  // 1、声明回调函数
  const vlidateFunc = (val) => {
    if (!val) {
      // console.log('success')
      let key = ''
      if (type === 'ele') {
        key = data.pageId + '-' + data.elementId + '-' + data.vlidateList[index].key
      } else if (type === 'page') {
        key = data.pageId + '-' + data.vlidateList[index].key
      } else {
        key = data.vlidateList[index].key
      }
      store.dispatch('cms/works/clearErrList', { key: key })
    } else {
      // 临时版本本，直接提示
      if (errlength > 0) return
      let msg = ''
      let title = ''
      if (type === 'work') {
        title = '【作品设置】'
        msg = `${val.message}`
      } else if (type === 'ele') {
        title = `【${eleData.element_name}】`
        msg = `${val.message}`
      } else {
        title = `【${pageData.name}】`
        msg = `${val.message}`
      }
      hMessage.error({
        closable: true,
        customerClass: 'msgContent',
        content: `<span class="msgContent_title">${title} </span> <p class="msgContent_content">${msg}</p>`, // `${pageName} ${comTips}未配置完成，${cusTips} 请完成配置后再发布`,
        duration: 5
      })
      errlength++
      // 正式版本，需要收集错误提示
      // let errObject = {
      //   type: type,
      //   message: val.message
      // }
      // // 将错误插入全局提示内容中
      // if (type === 'ele') {
      //   console.log(type, data.pageId, data.elementId, val.message)
      //   Object.assign(errObject, {
      //     key: data.pageId + '-' + data.elementId + '-' + data.vlidateList[index].key,
      //     elementId: data.elementId,
      //     pageId: data.pageId
      //   })
      // } else if (type === 'page') {
      //   console.log(type, data.pageId, val.message)
      //   Object.assign(errObject, {
      //     key: data.pageId + '-' + data.vlidateList[index].key,
      //     pageId: data.pageId
      //   })
      // } else {
      //   console.log(type, val.message)
      // }
      // store.dispatch('cms/works/updateErrList', errObject)
      // errlength++
    }
    index++
  }
  // 2、执行验证函数
  for (let i = 0; i < data.vlidateList.length; i++) {
    data.vlidateList[i].validateFunc('', data.vlidateList[i].value, vlidateFunc)
  }
  if (errlength > 0) {
    result = false
  }
  // 3、返回验证结果
  return result
}

/**
 * 作品全局表单验证
 * @return
 */
export function worksAdminVlidate(store) {
  let result = true
  let worksPassValidate = store.works.passValidate
  // 删除06版本之前的废弃数据
  console.log(worksPassValidate)
  if (worksPassValidate.items) {
    worksPassValidate = omit(worksPassValidate, ['items'])
  }
  // 1、执行验证函数
  if (worksPassValidate.vlidateFunc !== '') {
    // 验证作品信息
    if (!worksPassValidate.vlidateFunc()) {
      result = false
    }
  }
  if (keys(worksPassValidate).length > 1) {
    let object = omit(worksPassValidate, ['vlidateFunc'])
    // 验证组件与页面信息
    for (const key in object) {
      const page = object[key]
      if (page.vlidateFunc !== '') {
        // 验证页面信息
        if (!page.vlidateFunc()) {
          result = false
        }
      }
      if (keys(page).length > 1) {
        let eles = omit(page, ['vlidateFunc'])
        for (const i in eles) {
          const ele = eles[i]
          if (ele.vlidateFunc !== '') {
            // 验证组件信息
            if (!ele.vlidateFunc()) {
              result = false
            }
          }
        }
      }
    }
  }
  // 3、输出结果
  return result
}

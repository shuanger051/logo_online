/*
 * @Author: your name
 * @Date: 2021-11-09 10:27:00
 * @LastEditTime: 2021-11-12 16:19:12
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \前端埋点\UCP-FRAME\src\biz\cms\src\editor\core\runtime_context\runtime_plugins\trackPlugin.js
 */
/**
 * 数据埋点
 */

const install = (api) => {
  let {eventController} = api
  let { systemSign } = eventController
  if (!config.isProduction) {
    return
  }
  eventController.on(systemSign.component.click, () => {
    // 元素点击埋点
    console.log('埋点：', systemSign.component)
  })
}

const config = {
  // 标记是否是生产环境
  isProduction: window.__cmsProduction
}

export default {
  install,
  config
}

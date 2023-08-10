
import Vue from 'vue';
let mode = ''
let router = null
// 初始化ii8
const initI18n = (i18n) =>  {
  Vue.prototype.$i18nInstance = i18n
}
// 初始化请求
const initRequest = (request) =>  {
  Vue.prototype.$request = request
}
// 初始化模式
const initMode = (m) => {
  mode = m
}
// 初始化路由
const initRouter = (r) => {
  router = r
}

const createConfig = (factory) => {
   const {
      installHandler = {},
      instanceHandler = {}
   } = factory()
   return {
      install(fn) {
        fn({
          initRequest,
          initI18n,
          initRouter,
          initMode,
          ...installHandler
        })
        Vue.prototype.$editorConfig = this
        window.EditorApp = new Vue();
        window.$editorConfig = this
      },
      get router() {
        return router
      },
      isApp() {
        return mode == 'app'
      },
      ...instanceHandler
   }
}
export default createConfig

import Vue from 'vue';

const initI18n = (i18n) =>  {
  Vue.prototype.$i18nInstance = i18n
}
const initRequest = (request) =>  {
  Vue.prototype.$request = request
}
let mode = ''
const initMode = (m) => {
  mode = m
}
let router = null
const initRouter = (r) => {
  router = r
}
let handlerSaveSucessJump = () => {}
const initSaveSucessJump = (fn) => {
  handlerSaveSucessJump = fn
}
export default {
  install(fn) {
    fn({
      initRequest,
      initI18n,
      initRouter,
      initMode,
      initSaveSucessJump
    })
  },
  get router() {
    return router
  },
  get mode() {
    return mode
  },
  get handlerSaveSucessJump() {
    return handlerSaveSucessJump
  }
}
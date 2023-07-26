
import Vue from 'vue';

const initI18n = (i18n) =>  {
  Vue.prototype.$i18nInstance = i18n
}
const initRequest = (request) =>  {
  Vue.prototype.$request = request
}

export default {
  install(fn) {
    fn({
      initRequest,
      initI18n
    })
  }
}
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import i18n from './locales'
import './plugins/index'


new Vue({
  router,
  i18n,
  render: h => {
    return h(App)
  }
}).$mount('#app')

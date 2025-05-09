import Vue from "vue";
import App from "./App.vue";
import { initRouter } from "./router";
import VueI18n from 'vue-i18n'
import "./theme/index.less";
import Antd from "ant-design-vue";
import store from "./store";
import "animate.css/source/animate.css";
import * as axios from "@/utils/request";
import adminConfig from "core/adminAppConfig";
import * as shareAPI from '@/services/share'
import _ from "lodash"
window._ = _;
const router = initRouter();
function initI18n(locale, fallback) {
  Vue.use(VueI18n)
  let i18nOptions = {
    locale,
    fallbackLocale: fallback,
    silentFallbackWarn: true,
  }
  return new VueI18n(i18nOptions)
}
const i18n = initI18n("CN");

// 初始化编辑器配置
adminConfig.install(({ initRequest, initMode,initI18n, initRouter, initSaveSucessJump, extraFn}) => {
  initRequest(axios);
  initMode('admin');
  initRouter(router)
  initI18n(i18n);

  initSaveSucessJump(() => {
    if ( !router.currentRoute?.params?.id) {
      router.push('/signboard/template')
    }
  })
  extraFn(shareAPI)
});

Vue.use(Antd);
Vue.config.productionTip = false;

function initMenu(){  
  const rootRoute = router.options.routes.find(item => item.path === '/')
  const menuRoutes = rootRoute && rootRoute.children
  if (menuRoutes) {
    store.commit('setting/setMenuData', menuRoutes)
  }
}
initMenu()
new Vue({
  router,
  store,
  i18n,
  render: (h) => h(App),
}).$mount("#app");

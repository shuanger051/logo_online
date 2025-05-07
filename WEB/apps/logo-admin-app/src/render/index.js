import Vue from "vue";
import App from "./App.vue";
import { initRouter } from "./router";
import VueI18n from 'vue-i18n'
import AsyncImage from "./AsyncImage";
import "./theme/index.less";
import Antd from "ant-design-vue";
import store from "./store";
import "animate.css/source/animate.css";
import * as axios from "@/utils/request";
import adminConfig from "core/adminConfig";

const router = initRouter();
AsyncImage
// 初始化编辑器配置
adminConfig.install(({ initRequest, initMode, initRouter, initSaveSucessJump }) => {
  initRequest(axios);
  initMode('admin');
  initRouter(router)
  initSaveSucessJump(() => {
    if ( !router.currentRoute?.params?.id) {
      router.push('/signboard/template')
    }
  })
});

Vue.use(Antd);
Vue.config.productionTip = false;

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

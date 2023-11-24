import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import "./core/lazy_use";
import "./core/filters";
import "./core/directives";
import "lib-flexible";
import "./styles/iconfont/iconfont.css";
import router from "./router";
import store from "./store";
import appConfig from "core/appConfig";
import * as axios from "@/utils/request";
import { getEnvByUa } from "./utils/util";
import vconsole from "vconsole";
Vue.config.productionTip = false;

appConfig.install(({ initRequest, initMode, initRouter }) => {
  initMode("app");
  initRouter(router);
  initRequest(axios);
});

if (process.env.NODE_ENV !== "production") {
  new vconsole();
}

const app = new Vue({
  router,
  store,
  render: (h) => h(App),
});

// h5版
if (getEnvByUa() == "h5") app.$mount("#app");
// 浙里办
else
  ZWJSBridge.onReady(() => {
    // 判断是否适配适老版
    ZWJSBridge.getUiStyle().then((result) => {
      const { uiStyle } = result;
      console.log(uiStyle)
      const val = uiStyle == "elder";
      store.commit("app/setIsOldVersion", val);
    });
    app.$mount("#app");
  });

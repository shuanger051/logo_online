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
import evnetBus from "./core/eventBus";

import vconsole from "vconsole";
Vue.config.productionTip = false;
Vue.prototype.__eventBus = evnetBus;
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

app.$mount("#app");

// 权利阳光适配
const bridgeClient = new formbridgeClient();
bridgeClient.onReady(() => {
  // 不一致表明被ifram引用
  const isInIframe = window.self !== window.top;
  store.commit("app/setIsInIframe", isInIframe);
});

// 浙里办需要适配适老版
if (getEnvByUa() != "h5") {
  ZWJSBridge.onReady(() => {
    // 判断是否适配适老版
    ZWJSBridge.getUiStyle().then((result) => {
      const { uiStyle } = result;
      const val = uiStyle == "elder";
      store.commit("app/setIsOldVersion", val);
    });
  });
}

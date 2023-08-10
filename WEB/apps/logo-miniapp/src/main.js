import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import "./core/lazy_use";
import "lib-flexible";
import router from "./router";
import store from "./store";
import appConfig from "core/appConfig";
import * as axios from "@/utils/request";

Vue.config.productionTip = false;
appConfig.install(({ initRequest, initMode, initRouter }) => {
  initMode('app')
  initRouter(router)
  initRequest(axios);
})

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");

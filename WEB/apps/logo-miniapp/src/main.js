import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import "./core/lazy_use";
import "./core/filters";
import "lib-flexible";
import "./styles/iconfont/iconfont.css";
import router from "./router";
import store from "./store";
import appConfig from "core/appConfig";
import * as axios from "@/utils/request";
import VConsole from 'vconsole';

Vue.config.productionTip = false;

if (process.env.NODE_ENV !== "production") {
 // const vConsole = new VConsole();
}
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

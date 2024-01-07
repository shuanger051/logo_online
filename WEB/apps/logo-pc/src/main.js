import Vue from "vue";
import App from "./App.vue";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css"; 
import router from "./router";
import store from "./store";
import appConfig from "core/appConfig";
import * as axios from "@/utils/request";



appConfig.install(({ initRequest, initMode, initRouter }) => {
  initMode("pc");
  initRouter(router);
  initRequest(axios);
});

Vue.use(Antd);

const app = new Vue({
  router,
  store,
  render: (h) => h(App),
});

app.$mount("#app")
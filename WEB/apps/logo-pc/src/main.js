import Vue from "vue";
import App from "./App.vue";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css"; 
import "./core/filters";
import "./styles/iconfont/iconfont.css";
import router from "./router";
import store from "./store";
import pcConfig from "core/pcConfig";
import * as axios from "@/utils/request";



pcConfig.install(({ initRequest, initMode, initRouter }) => {
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

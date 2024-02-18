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

// 权利阳光适配
const bridgeClient = new formbridgeClient();
bridgeClient.onReady(() => {
  // 不一致表明被ifram引用
  const isInIframe = window.self !== window.top;
  store.commit("app/setIsInIframe", isInIframe);
});

const app = new Vue({
  router,
  store,
  render: (h) => h(App),
});

app.$mount("#app")

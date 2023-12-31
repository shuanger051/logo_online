import Vue from "vue";
import App from "./App.vue";
import { initRouter } from "./router";
import "./theme/index.less";
import Antd from "ant-design-vue";
import Viser from "viser-vue";
import store from "./store";
import "animate.css/source/animate.css";
import Plugins from "@/plugins";
import { initI18n } from "@/utils/i18n";
import bootstrap from "@/bootstrap";
import * as axios from "@/utils/request";
import adminConfig from "core/adminConfig";

const router = initRouter(store.state.setting.asyncRoutes);
const i18n = initI18n("CN");

// 初始化编辑器配置
adminConfig.install(({ initI18n, initRequest, initMode, initRouter, initSaveSucessJump }) => {
  initI18n(i18n);
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
Vue.use(Viser);
Vue.use(Plugins);
bootstrap({ router, store, i18n, message: Vue.prototype.$message });

new Vue({
  router,
  store,
  i18n,
  render: (h) => h(App),
}).$mount("#app");

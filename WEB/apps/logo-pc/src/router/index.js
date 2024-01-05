import Vue from "vue";
import VueRouter from "vue-router";
import signboard from "./signboard";

Vue.use(VueRouter);

const routes = [
  // 店招模块
  signboard,
  // 404
  // {
  //   path: "/*",
  //   name: "404",
  //   redirect: "/",
  // },
];

const router = new VueRouter({
  mode: "hash",
  base: '/',
  routes,
});

export default router;

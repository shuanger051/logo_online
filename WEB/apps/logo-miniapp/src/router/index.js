import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import signboardRoute from "./modules/signboard";
import articleRoute from "./modules/article";
import shopRoute from "./modules/shop";
import materialRoute from './modules/material'
import sampleRoute from "./modules/sample";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    meta: { title: "菜单式店招设计", isCanBack: false },
    component: Home,
  },
  {
    path: "/login",
    name: "login",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/account/login.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/account/register.vue"),
  },
  // 商铺管理
  shopRoute,
  // 店招模块
  signboardRoute,
  // 文章模块
  articleRoute,
  // 材质参考
  materialRoute,
  // 示例图
  sampleRoute,
  // 404
  {
    path: "/*",
    name: "404",
    redirect: "/",
  },
];

const router = new VueRouter({
  mode: "hash",
  base: "/",
  routes,
});

export default router;

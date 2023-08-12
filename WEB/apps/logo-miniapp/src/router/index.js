import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import signboardRoute from "./modules/signboard";
import articleRoute from "./modules/article";
import shopRoute from "./modules/shop";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/login",
    name: "login",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/account/login.vue"),
  },
  {
    path: "/editTemplate/:id?",
    name: "addTemplate",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/editTemplate/edit.vue"),
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
  // 404
  {
    path: "/*",
    name: "404",
    redirect: "/",
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;

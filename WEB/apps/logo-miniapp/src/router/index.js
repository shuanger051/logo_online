import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import articleRoute from "./modules/article";

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
    path: "/register",
    name: "register",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/account/register.vue"),
  },
  // 文章模块
  articleRoute,
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;

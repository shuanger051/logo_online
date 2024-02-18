import Vue from "vue";
import VueRouter from "vue-router";
import signboard from "./signboard";
import article from "./article";
import Home from "../views/home";
import Entrepot from "../views/Entrepot";

Vue.use(VueRouter);

const routes = [
  // 首页
  {
    path: "/",
    name: "Home",
    meta: { title: "菜单式店招设计" },
    component: Home,
  },
  // 店招模块
  signboard,
  // 文章模块
  article,
  // 重定向
  {
    path: "/entrepot",
    component: Entrepot,
  },
  // 404
  // {
  //   path: "/*",
  //   name: "404",
  //   redirect: "/",
  // },
];

const router = new VueRouter({
  mode: "hash",
  base: "/",
  routes,
});

export default router;

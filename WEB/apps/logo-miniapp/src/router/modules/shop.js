import { BasicLayout } from "@/layouts";

const routes = [
  {
    path: "list",
    meta: { title: "我的商铺" },
    component: () => import("@/views/shop/list"),
  },
  {
    path: "owner",
    meta: { title: "商户登记" },
    component: () => import("@/views/shop/owner"),
  },
  {
    path: "detail",
    meta: { title: "商铺信息" },
    component: () => import("@/views/shop/form"),
  },
];

// 文章模块路由
export default {
  path: "/shop",
  name: "shop",
  redirect: "/shop/list",
  component: BasicLayout,
  children: routes,
};

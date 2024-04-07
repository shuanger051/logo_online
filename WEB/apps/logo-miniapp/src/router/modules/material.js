import { BlankLayout } from "@/layouts";

const routes = [
  {
    path: "list",
    name: "material/list",
    meta: { title: "材质参考" },
    component: () => import("@/views/material/list.vue"),
  },
  {
    path: "detail",
    name: "material/detail",
    meta: { title: "材质参考" },
    component: () => import("@/views/material/detail.vue"),
  },
];

// 文章模块路由
export default {
  path: "/material",
  name: "material",
  redirect: "/material/list",
  component: BlankLayout,
  children: routes,
};

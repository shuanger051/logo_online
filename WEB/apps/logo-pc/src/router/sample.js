import BlankLayout from "@/layouts/BlankLayout";

const routes = [
  {
    path: "list",
    name: "sample/list",
    meta: { title: "精品素材" },
    component: () => import("@/views/sample/list.vue"),
  },
  {
    path: "detail",
    name: "sample/detail",
    meta: { title: "精品素材" },
    component: () => import("@/views/sample/detail.vue"),
  },
];

// 文章模块路由
export default {
  path: "/sample",
  name: "sample",
  meta: { name: "精品素材" },
  redirect: "/sample/list",
  component: BlankLayout,
  children: routes,
};

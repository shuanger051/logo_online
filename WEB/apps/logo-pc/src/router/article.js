import BlankLayout from "@/layouts/BlankLayout";

const routes = [
  {
    path: ":channelId/list",
    name: "article/list",
    meta: { title: "文章列表" },
    component: () => import("@/views/article/list.vue"),
  },
  {
    path: ":channelId/detail",
    name: "article/detail",
    meta: { title: "文章详情" },
    component: () => import("@/views/article/detail.vue"),
  },
];

// 文章模块路由
export default {
  path: "/article",
  name: "article",
  redirect: "/article/4/list",
  component: BlankLayout,
  children: routes,
};

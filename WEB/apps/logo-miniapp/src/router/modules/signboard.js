import { BasicLayout } from "@/layouts";

const routes = [
  // 店招模版选择页
  {
    path: "template",
    meta: { title: "模版选择" },
    component: () => import("@/views/signboard/template"),
  },
  {
    path: "design",
    meta: { title: "店招设计" },
    component: () => import("@/views/shop/form"),
  },
  {
    path: "uploadLive",
    meta: { title: "实景上传" },
    component: () => import("@/views/signboard/uploadLive"),
  },
  {
    path: "editLive",
    meta: { title: "实景编辑" },
    component: () => import("@/views/signboard/editLive"),
  },
  {
    path: "/editSignboard/:id?",
    name: "editSignboard",
    component: () =>
      import(/* webpackChunkName: "about" */ "@/views/signboard/editSignboard.vue"),
  },
];

// 店招模块路由
export default {
  path: "/signboard",
  name: "signboard",
  component: BasicLayout,
  children: routes,
};

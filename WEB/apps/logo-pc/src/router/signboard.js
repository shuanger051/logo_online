import { BlankLayout } from "@/layouts";

const routes = [
  // 店招模版选择页
  {
    path: "template",
    meta: { title: "模版选择" },
    component: () => import("@/views/signboard/template"),
  },
  // 店招属性选择
  {
    path: "attribute",
    meta: { title: "类型选择" },
    component: () => import("@/views/signboard/attribute"),
  },
  {
    path: "editSelect",
    meta: { title: "编辑选择" },
    component: () => import("@/views/signboard/editSelect"),
  },

  // {
  //   path: "selfEdit",
  //   name: 'selfEdit',
  //   meta: { title: "自主设计" },
  //   component: () => import("@/views/signboard/selfEdit"),
  // },
  {
    path: "editLive",
    name: 'editLive',
    meta: { title: "实景编辑" },
    component: () => import("@/views/signboard/editLive"),
  },
  // {
  //   path: "editConfirm",
  //   name: 'editConfirm',
  //   meta: { title: "备案确认" },
  //   component: () => import("@/views/signboard/editConfirm"),
  // },
  // {
  //   path: "intelligenceTemplate",
  //   name: 'intelligenceTemplate',
  //   meta: { title: "模板选择" },
  //   component: () => import("@/views/signboard/intelligenceTemplate"),
  // },
  // {
  //   path: "intelligenceDesign",
  //   name: 'intelligenceDesign',
  //   meta: { title: "智能设计" },
  //   component: () => import("@/views/signboard/intelligenceDesign"),
  // },
  {
    path: "editSignboard/:id?",
    name: "editSignboard",
    meta: { title: "店招编辑" },
    component: () =>
      import("@/views/signboard/editSignboard.vue"),
  },
];

// 店招模块路由
export default {
  path: "/signboard",
  name: "signboard",
  component: BlankLayout,
  children: routes,
};

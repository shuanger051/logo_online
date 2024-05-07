import { BlankLayout } from "@/layouts";
import store from "@/store";

const routes = [
  // 负面清单
  {
    path: "negative",
    meta: { title: "户外招牌设置负面清单" },
    component: () => import("@/views/signboard/negativeList"),
    beforeEnter: (from, to, next) => {
      const isRead = _.get(store.state, "app.isReadNegative");
      // 根据条件跳转
      if (isRead) next({ path: "/signboard/streetTypeSelect" });
      else next();
      return
    },
  },
  // 店招模版选择页
  {
    path: "template",
    meta: { title: "模版选择" },
    component: () => import("@/views/signboard/template"),
  },
  // 店招属性选择
  {
    path: "attribute",
    name: 'attribute',
    meta: { title: "类型选择" },
    component: () => import("@/views/signboard/attribute"),
  },
  {
    path: "editSelect",
    meta: { title: "编辑选择" },
    component: () => import("@/views/signboard/editSelect"),
  },
  // 街区类型
  {
    path: "streetTypeSelect",
    meta: { title: "街区类型" },
    component: () => import("@/views/signboard/streetTypeSelect"),
  },
  // 街区道路
  {
    path: "streetSelect",
    meta: { title: "一街一景"},
    component: () => import("@/views/signboard/streetSelect"),
  },
  {
    path: "streetIntro",
    meta: { title: "一街一景" },
    component: () => import("@/views/signboard/streetIntro"),
  },
  {
    path: "design",
    meta: { title: "店招设计" },
    component: () => import("@/views/shop/form"),
  },
  {
    path: "selfEdit",
    name: "selfEdit",
    meta: { title: "自主设计" },
    component: () => import("@/views/signboard/selfEdit"),
  },
  {
    path: "editLive",
    name: "editLive",
    meta: { title: "实景编辑" },
    component: () => import("@/views/signboard/editLive"),
  },
  {
    path: "editConfirm",
    name: "editConfirm",
    meta: { title: "备案确认" },
    component: () => import("@/views/signboard/editConfirm"),
  },
  {
    path: "intelligenceTemplate",
    name: "intelligenceTemplate",
    meta: { title: "模板选择" },
    component: () => import("@/views/signboard/intelligenceTemplate"),
  },
  {
    path: "intelligenceDesign",
    name: "intelligenceDesign",
    meta: { title: "智能设计" },
    component: () => import("@/views/signboard/intelligenceDesign"),
  },
  {
    path: "editSignboard/:id?",
    name: "editSignboard",
    meta: { title: "店招编辑" },
    component: () => import("@/views/signboard/editSignboard.vue"),
  },
];

// 店招模块路由
export default {
  path: "/signboard",
  name: "signboard",
  component: BlankLayout,
  children: routes,
};

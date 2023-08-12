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
];

// 店招模块路由
export default {
  path: "/signboard",
  name: "signboard",
  component: BasicLayout,
  children: routes,
};

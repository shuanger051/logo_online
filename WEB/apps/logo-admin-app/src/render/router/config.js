import TabsView from "@/layouts/tabs/TabsView";
import PageView from "@/layouts/PageView";

// 路由配置
const options = {
  routes: [
    {
      path: "/",
      name: "首页",
      component: TabsView,
      redirect: "/login",
      children: [
        {
          path: "home",
          name: "首页",
          meta: {
            icon: "file-ppt",
          },
          component: () => import("@/pages/demo"),
        },
        // {
        //   path: "signboard",
        //   name: "店招",
        //   meta: {
        //     icon: "picture",
        //   },
        //   component: PageView,
        //   children: [
 
            {
              path: "template",
              name: "模版管理",
              component: () => import("@/pages/signboard/template/list"),
              meta: {
                icon: "picture",
              },
            },
            {
              path: "/addTemplate/:id?",
              name: "新建模板",
              component: () => import("@/pages/signboard/template/addTemplate"),
              meta: {
                invisible: true,
                cacheAble: false,
              },
            },
            {
              path: "article",
              name: "文章管理",
              component: () => import("@/pages/affiche/article/list"),
            },
        //   ],
        // },
        {
          path: "system",
          name: "系统管理",
          meta: {
            icon: "setting",
          },
          component: PageView,
          // children: [
          // ],
        },
      ],
    },
    {
      path: "/login",
      name: "登录页",
      component: () => import("@/pages/login"),
    },
    {
      path: "/403",
      name: "403",
      component: () => import("@/pages/exception/403"),
    },
    {
      path: "*",
      name: "404",
      component: () => import("@/pages/exception/404"),
    },
  ],
};

export default options;

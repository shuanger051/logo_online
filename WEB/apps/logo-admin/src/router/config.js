import TabsView from '@/layouts/tabs/TabsView'
// import BlankView from '@/layouts/BlankView'
import PageView from '@/layouts/PageView'

// 路由配置
const options = {
  routes: [
    {
      path: '/',
      name: '首页',
      component: TabsView,
      redirect: '/login',
      children: [
        {
          path: 'home',
          name: '首页',
          meta: {
            icon: 'file-ppt'
          },
          component: () => import('@/pages/demo')
        },
        {
          path: 'system',
          name: '系统管理',
          meta: {
            icon: 'setting',
          },
          component: PageView,
          children: [
            {
              path: 'user',
              name: '用户管理',
              component: () => import('@/pages/system/user/list'),
            },
            {
              path: 'role',
              name: '角色管理',
              component: () => import('@/pages/system/role/list'),
            },
            {
              path: 'authority',
              name: '权限管理',
              component: () => import('@/pages/system/authority/list'),
            },
            {
              path: 'dict',
              name: '字典管理',
              component: () => import('@/pages/system/dict/list'),
            }
          ]
        },
        {
          path: 'shop',
          name: '商户管理',
          meta: {
            icon: 'shop'
          },
          component: PageView,
          children: [
            {
              path: 'owner',
              name: '商户信息',
              component: () => import('@/pages/shop/owner/list'),
            },
            {
              path: 'shop',
              name: '商铺信息',
              component: () => import('@/pages/shop/shop/list'),
            }
          ]
        },
        {
          path: 'signboard',
          name: '店招管理',
          meta: {
            icon: 'picture'
          },
          component: PageView,
          children: [
            {
              path: 'template',
              name: '模版管理',
              component: () => import('@/pages/signboard/template/list'),
            },
            {
              path: '/addTemplate',
              name: '新建模板',
              component: () => import('@/pages/signboard/template/addTemplate'),
            },
            {
              path: 'apply',
              name: '商户店招管理',
              component: () => import('@/pages/signboard/apply/list'),
            },
            {
              path: 'material',
              name: '素材管理',
              component: () => import('@/pages/signboard/material/list'),
            }
          ]
        },
        {
          path: 'affiche',
          name: '公告管理',
          meta: {
            icon: 'notification'
          },
          component: PageView,
          children: [
            {
              path: 'column',
              name: '栏目管理',
              component: () => import('@/pages/affiche/column/list'),
            },
            {
              path: 'inform',
              name: '通知管理',
              component: () => import('@/pages/affiche/inform/list'),
            },
            {
              path: 'policy',
              name: '政策法规',
              component: () => import('@/pages/affiche/policy/list'),
            }
          ]
        }, {
          path: 'logs',
          name: '日志管理',
          meta: {
            icon: 'bug'
          },
          component: () => import('@/pages/demo')
        },
        // {
        //   path: 'exception',
        //   name: '异常页',
        //   meta: {
        //     icon: 'warning',
        //   },
        //   component: BlankView,
        //   children: [
        //     {
        //       path: '404',
        //       name: 'Exp404',
        //       component: () => import('@/pages/exception/404')
        //     },
        //     {
        //       path: '403',
        //       name: 'Exp403',
        //       component: () => import('@/pages/exception/403')
        //     },
        //     {
        //       path: '500',
        //       name: 'Exp500',
        //       component: () => import('@/pages/exception/500')
        //     }
        //   ]
        // },
        {
          name: '验权页面',
          path: 'auth/demo',
          meta: {
            icon: 'file-ppt',
            authority: {
              permission: 'form',
              role: 'manager'
            },
            component: () => import('@/pages/demo')
          }
        }
      ]
    },
    {
      path: '/login',
      name: '登录页',
      component: () => import('@/pages/login')
    },
    {
      path: '/403',
      name: '403',
      component: () => import('@/pages/exception/403'),
    },
    {
      path: '*',
      name: '404',
      component: () => import('@/pages/exception/404'),
    },
  ]
}

export default options

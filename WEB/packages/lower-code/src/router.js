import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/editor/:workId', // #!zh 编辑器页面，核心功能部分
      name: 'editor',
      component: () => import('./views/Editor.vue')
    }
  ]
})

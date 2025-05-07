/**
 * 格式化路由
 * @param routes 路由配置
 */
function formatRoutes(routes) {
  routes.forEach(route => {
    const {path} = route
    if (!path.startsWith('/') && path !== '*') {
      route.path = '/' + path
    }
  })
}
function getI18nKey(path) {
  const keys = path.split('/').filter(item => !item.startsWith(':') && item != '')
  keys.push('name')
  return keys.join('.')
}

export { formatRoutes,getI18nKey }

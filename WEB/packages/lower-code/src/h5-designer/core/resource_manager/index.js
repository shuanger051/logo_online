
import loader from './loader'
import ResourceContainer from './ResourceContainer'

const pathsStore = new Set()

/**
 * 格式化加载的资源
 */
const formart = (resource) => { return resource }

/**
 * 资源管理器
 * 提供资源容器和安装功能
 */
export default {
  /**
   * 资源安装接口
   * @param {*} urls
   * @returns
   */
  install(urls) {
    let path = []
    urls.forEach(url => {
      if (!pathsStore.has(url)) {
        path.push(url)
      }
    })

    return (
      loader(path)
        .then((data) => {
          data.forEach(rowData => {
            let resource = formart(rowData)
            this.container.addResources(resource)
          })

          return this.container
        })
    )
  },

  /**
   * 资源容器获取
   */
  get container () {
    return ResourceContainer
  }
}

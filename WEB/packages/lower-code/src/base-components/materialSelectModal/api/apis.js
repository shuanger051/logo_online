import {apiPost} from './request'

export default {
  // 检测物料名称是否重复
  existsMaterielByCatalogIdAndName(params, url) {
    return apiPost(params, url)
  },
  // 新增物料审核信息
  postMmsMaterielAudit(params, url) {
    return apiPost(params, url)
  },
  // 查询标签列表
  getTagList(url) {
    return apiPost(url)
  },
  // 获取全部菜单列表增加权限角色逻辑的过滤
  getMaterialCatalogList(url) {
    return apiPost(url)
  },
  // 素材库上传弹窗--查询列表
  getMaterialStoreUploadList(params, url) {
    return apiPost(params, url)
  },
  // 获取配置项信息
  getSystemConfigInfo(url) {
    return apiPost(url)
  },
  // 获取自定义标签分类
  getCustomTagClass(url) {
    return apiPost(url)
  }
}

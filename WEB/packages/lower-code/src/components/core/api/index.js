import Vue from 'vue'

const wrapRequest = (url, isPost, config) => {
  return (...args) => {
    const request = Vue.prototype.$request
   return request[isPost ? 'axiosPost' : 'axiosGet'](url, config)(...args)
  }
}
export const saveMaterial = wrapRequest('/logo/material/saveMaterial', true)
export const getMaterialListByPage = wrapRequest('/logo/material/getMaterialListByPage', false)
export const saveTemplate = wrapRequest('/logo/template/saveTemplate', true)
export const updateTemplate = wrapRequest('/logo/template/updateTemplate', true)
export const adminGetTemplateByID = wrapRequest('/logo/template/getTemplateByID', false)
export const uploadMaterialAttachment =  wrapRequest('logo/attachment/uploadMaterialAttachment', true)
export const getDictById = wrapRequest('/logo/sys/dict-item/getItemsByDictKey', false)
export const getTemplateByID = async (...arg) => {
  if (window.$editorConfig.isApp())  {
    return appQueryTemplate(...arg)
  } else {
    return adminGetTemplateByID(...arg)
  }
}


export const appGetMaterial = wrapRequest('/logo/app/getMaterialListByPageAPI?fileType=1', false)
export const appQueryTemplate = wrapRequest('logo/app/queryTemplateByIdAPI', false)
export const appSaveLogoInfoAPI = wrapRequest('/logo/app/saveLogoInfoAPI', true)
//APP 根据商铺ID获取店招信息API
export const appGetLogoInfoByShopsId = wrapRequest('/logo/app/getLogoInfoByShopsIdAPI', false)
export const appGetItemsByDictKeyInDB = wrapRequest('/logo/app/getItemsByDictKeyInDB', false)
export const appGetCustomerInfoByUserNameAPI = wrapRequest('/logo/app/getCustomerInfoByUserNameAPI', false)
export const appQuerySimpleTemplateByRandAPI = wrapRequest('/logo/app/querySimpleTemplateByRandAPI', false)

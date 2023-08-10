import Vue from 'vue'
import a from './test'
const request = Vue.prototype.$request
export const saveMaterial = request.axiosPost('/logo/material/saveMaterial')
export const getMaterialListByPage = request.axiosGet('/logo/material/getMaterialListByPage')
export const saveTemplate = request.axiosPost('/logo/template/saveTemplate')
export const updateTemplate = request.axiosPost('/logo/template/updateTemplate')
// export const getTemplateByID = request.axiosGet('/logo/template/getTemplateByID')
export const uploadMaterialAttachment =  request.axiosPost('logo/attachment/uploadMaterialAttachment')

export const getTemplateByID = async () => {
  return a
}

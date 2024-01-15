import Vue from "vue";

const wrapRequest = (url, isPost, config) => {
  return (...args) => {
    const request = Vue.prototype.$request;
    return request[isPost ? "axiosPost" : "axiosGet"](url, config)(...args);
  };
};
export const saveMaterial = wrapRequest("/logo/material/saveMaterial", true);
export const getMaterialListByPage = wrapRequest(
  "/logo/material/getMaterialListByPage",
  false
);
export const getMaterialListByPageOSS = wrapRequest(
  "/logo/material/getMaterialListByPageOSS",
  false
);
export const saveTemplate = wrapRequest("/logo/template/saveTemplate", true);
export const updateTemplate = wrapRequest(
  "/logo/template/updateTemplate",
  true
);

export const getMaterialByID =  wrapRequest("/logo/material/getMaterialByID", false)
export const adminGetTemplateByID = wrapRequest(
  "/logo/template/getTemplateByID",
  false
);
export const uploadMaterialAttachment = wrapRequest(
  "logo/attachment/uploadMaterialAttachmentOSS",
  true
);
export const getDictById = wrapRequest(
  "/logo/sys/dict-item/getItemsByDictKey",
  false
);
export const getTemplateByID = async (...arg) => {
  if (window.$editorConfig.isApp()) {
    return appQueryTemplate(...arg);
  } else {
    return adminGetTemplateByID(...arg);
  }
};

export const appGetMaterial = wrapRequest(
  "/logo/app/getMaterialListByPageAPI?fileType=1",
  false
);
export const appQueryTemplate = wrapRequest(
  "logo/app/queryTemplateByIdAPI",
  false
);
export const appSaveLogoInfoAPIOSS = wrapRequest(
  "/logo/app/saveLogoInfoAPIOSS",
  true
);
export const appSaveLogoInfoBase64API = wrapRequest(
  "/logo/app/saveLogoInfoBase64API",
  true
);
export const appSaveLogoInfoBase64APIOSS = wrapRequest(
  "/logo/app/saveLogoInfoBase64APIOSS",
  true
);
export const appUploadContentAttachmentBase64OSS = wrapRequest(
  "/logo/app/uploadShopsContentAttachmentBase64APIOSS",
  true
);
export const appGetMaterialListByPageApiOSS = wrapRequest(
  "/logo/app/getMaterialListByPageAPIOSS",
  false
);

//APP 根据商铺ID获取店招信息API
export const appGetLogoInfoByShopsId = wrapRequest(
  "/logo/app/getLogoInfoByShopsIdAPI",
  false
);
export const appGetLogoInfoByShopsIdOSS = wrapRequest(
  "/logo/app/getLogoInfoByShopsIdAPIOSS",
  false
);
export const appGetItemsByDictKeyInDB = wrapRequest(
  "/logo/app/getItemsByDictKeyInDB",
  false
);
export const appGetCustomerInfoByUserNameAPI = wrapRequest(
  "/logo/app/getCustomerInfoByUserNameAPI",
  false
);
export const appQuerySimpleTemplateByRandAPI = wrapRequest(
  "/logo/app/querySimpleTemplateByRandAPI",
  false
);
export const appUploadShopsAttachmentAPI = wrapRequest(
  "/logo/app/uploadShopsAttachmentAPI",
  true
);
export const appUpdateShopsFilingsStatusAPI = wrapRequest(
  "/logo/app/updateShopsFilingsStatusAPI",
  true
);
export const appGetShopsInfoByIdAPI = wrapRequest(
  "/logo/app/getShopsInfoByIdAPI",
  false
);
export const appGetShopsInfoByIdAPIOSS = wrapRequest(
  "/logo/app/getShopsInfoByIdAPIOSS",
  false
);

export const appUploadMaterialAttachmentOSS = wrapRequest(
  "/logo/app/uploadMaterialAttachmentAPIOSS",
  true
);
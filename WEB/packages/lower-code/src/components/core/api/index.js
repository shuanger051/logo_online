import Vue from "vue";
import OSS from 'ali-oss';
const a = 'L_T_A_I_5_t_C_q_x';
const b = 'h_S_K_c_8_H_e_P_b_F_w_r_E_z_h'

const c = '_C_0_8_T_M_z_s_R_M_m_V'
const d = '_o_0_A_k_K_x_w_k_o_W_O_H_J_p_e_c_V_c_9';

const wrapRequest = (url, isPost, config) => {
  return (...args) => {
    const request = Vue.prototype.$request;
    return request[isPost ? "axiosPost" : "axiosGet"](url, config)(...args);
  };
};

const getLocalTemplateByID = (id) => {
  id = +id
  const template = window.template.data.list.find((item) => item.id === id);
  return Promise.resolve({
    data: template
  })
}
export const getlocalDictsKey = (params) => {
  let data = window.dicts[params.dictKey];
  return Promise.resolve({
    data: data.data || []
  })
}
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

export const updateTemplate = (data) => {
  return window.$editorConfig.getExtraObj().updateTemplateById(data)
}

export const getMaterialByID =  wrapRequest("/logo/material/getMaterialByID", false)
export const adminGetTemplateByID = (data) => {
  return window.$editorConfig.getExtraObj().getTemplateByID(data)
};
export const uploadMaterialAttachment = wrapRequest(
  "logo/attachment/uploadMaterialAttachmentOSS",
  true
);
export const getDictById = (data) => window.$editorConfig.getExtraObj().getItemsByDictKey(data)

export const getTemplateByID = async (...arg) => {
  if (window.$editorConfig.mode !='admin') {
    return appQueryTemplate(...arg);
  } else {
    return adminGetTemplateByID(...arg);
  }
};

export const appGetMaterial = wrapRequest(
  "/logo/app/getMaterialListByPageAPI?fileType=1",
  false
);

// export const appQueryTemplate = wrapRequest(
//   "logo/app/queryTemplateByIdAPI",
//   false
// );
export const appQueryTemplate = (data) => {
  return getLocalTemplateByID(data.id)
}
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

export const appGetItemsByDictKeyInDB = (params) => {
  return getlocalDictsKey(params)
}
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


 const bucket = 'new-img-save-dir';
 const region = 'oss-cn-hangzhou';
const client = new OSS({
  region:region,
  accessKeyId: (a+b).split('_').join(''),
  accessKeySecret: (c+d).split('_').join(''),
  bucket: bucket
});

const uploadFile = async (file,config = {}) => {
  const fileextension = file.name.split('.').pop().toLowerCase();
  const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp','xlsx'];
  if (!allowedExtensions.includes(fileextension)) {
    throw new Error('不允许上传的格式');
  }
  const year = new Date().getFullYear();
  const month = String(new Date().getMonth() + 1).padStart(2, '0');
  const day = String(new Date().getDate()).padStart(2, '0');
  const randomString = Math.random().toString(36).substring(2, 10);
  const filePath = 'upload/material/' + year + '/' + month + '/' + day + '/' + (config.name || randomString + '.' + fileextension);
  try{
  await client.put(filePath, file);
  } catch (e) {
    console.error('上传失败', e);
    throw e
  }
  return `https://${bucket}.${region}.aliyuncs.com/${filePath}`;
} 


export const appUploadMaterialAttachmentOSS = async (file, config) => {
  const url =  await uploadFile(file,config)
  console.log(url, 9999)
  return {
    data: {
      urlPath: url
    }
  }
}

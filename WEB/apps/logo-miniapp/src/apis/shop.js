import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 获取商铺信息 ==
 */
export const getCustomerInfoByUserNameAPI = axiosGet(
  "logo/app/getCustomerInfoByUserNameAPIOSS"
);

/**
 * == 获取商铺详情 ==
 */
export const getShopsInfoByIdAPI = axiosGet("logo/app/getShopsInfoByIdAPIOSS");

/**
 * == 登记商户信息 ==
 */
export const saveMerchantAPI = axiosPost("logo/app/saveMerchantAPI");

/**
 * == 修改商户信息 ==
 */
export const updateMerchantAPI = axiosPost("logo/app/updateMerchantAPI");

/**
 * == 登记商铺信息 ==
 */
export const saveShopsInfoAPI = axiosPost("logo/app/saveShopsInfoAPI");

/**
 * == 修改商铺信息 ==
 */
export const updateShopsInfoAPI = axiosPost("logo/app/updateShopsInfoAPI");

/**
 * == 上传商铺附件 ==
 */
export const uploadShopsAttachmentAPI = axiosPost(
  "logo/app/uploadShopsAttachmentAPIOSS",
  {
    headers: {
      "content-Type": "multipart/form-data",
    },
  }
);

/**
 * == 获取店招图 ==
 */
export const getLogoInfoByShopsIdAPI = axiosGet(
  "logo/app/getLogoInfoByShopsIdAPIOSS"
);

/**
 * == 获取证件照原图 ==
 */
export const getShopsIdCardByIdAPI = axiosGet("logo/app/getShopsIdCardByIdAPI");

/**
 * == 变更审核状态 ==
 */
export const updateShopsFilingsStatusAPI = axiosPost("logo/app/updateShopsFilingsStatusAPI")
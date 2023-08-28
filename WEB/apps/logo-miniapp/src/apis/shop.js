import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 获取商铺信息 ==
 */
export const getCustomerInfoByUserNameAPI = axiosGet(
  "logo/app/getCustomerInfoByUserNameAPI"
);

/**
 * == 获取商铺详情 ==
 */
export const getShopsInfoByIdAPI = axiosGet("logo/app/getShopsInfoByIdAPI");

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
  "logo/app/uploadShopsAttachmentAPI",
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
  "logo/app/getLogoInfoByShopsIdAPI"
);

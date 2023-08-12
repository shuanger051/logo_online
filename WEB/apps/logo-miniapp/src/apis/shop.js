import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 获取商铺信息 ==
 */
export const getCustomerInfoByUserNameAPI = axiosGet(
  "logo/app/getCustomerInfoByUserNameAPI"
);

/**
 * == 登记商户信息 ==
 */
export const saveMerchantAPI = axiosPost("logo/app/saveMerchantAPI");

/**
 * == 登记商铺信息 ==
 */
export const saveShopsInfoAPI = axiosPost("logo/app/saveShopsInfoAPI");

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

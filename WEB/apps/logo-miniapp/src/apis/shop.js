import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 获取商铺信息 ==
 */
export const getCustomerInfoByUserNameAPI = axiosGet(
  "logo/app/getCustomerInfoByUserNameAPI"
);

/**
 * == 等级商户信息 ==
 */
export const saveMerchantAPI = axiosPost("logo/app/saveMerchantAPI");

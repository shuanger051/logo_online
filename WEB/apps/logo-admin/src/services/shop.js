
import { axiosPost, axiosGet } from "../utils/request";

/** 分页查询商户列表 */
export const getMerchantInfoListByPage = axiosGet("/logo/merchantInfo/getMerchantInfoListByPage");
/** 根据ID查询商户信息 */
export const getMerchantInfoById = axiosGet("/logo/merchantInfo/getMerchantInfoById");
/** 新增商户信息 */
export const saveMerchantInfo = axiosPost("/logo/merchantInfo/saveMerchantInfo");
/** 修改商户信息 */
export const updateMerchantInfoById = axiosPost("/logo/merchantInfo/updateMerchantInfoById");
/** 删除商户信息 */
export const deleteMerchantInfoById = axiosPost("/logo/merchantInfo/deleteMerchantInfoById");


/** 分页查询店铺信息列表 */
export const getShopsInfoListByPage = axiosGet("/logo/shops-info/getShopsInfoListByPageOSS");
/** 根据店铺ID查询店铺信息 */
export const getShopsInfoById = axiosGet("/logo/shops-info/getShopsInfoById");
/** 上传商铺附件 */
export const uploadShopsAttachment = axiosPost("/logo/attachment/uploadShopsAttachment");
/** 根据附件名称删除商铺附件 */
export const deleteShopsAttachment = axiosPost("/logo/shops-info/deleteShopsAttachment");
/** 新增商铺信息 */
export const saveShopsInfo = axiosPost("/logo/shops-info/saveShopsInfo");
/** 修改店铺信息 */
export const updateShopsInfoById = axiosPost("/logo/shops-info/updateShopsInfoById");
/** 删除店铺信息 */
export const deleteShopsInfoById = axiosPost("/logo/shops-info/deleteShopsInfoById");
/** 审核状态变更 */
export const updateShopsFilingsStatusAPI = axiosPost("/logo/shops-info/shops-info/updateShopsFilingsStatus")
import { axiosPost, axiosGet } from "../utils/request";

/** 登录接口 */
export const login = axiosPost("/logo/sys/user/login");
/** 登出接口 */
export const logout = axiosGet("/logo/sys/user/logout");
/** 获取RSA的公钥 */
export const getPublicKey = axiosGet("/logo/crypto/getPublicKey");
/** 利用RSA公钥加密 */
export const encrypt = axiosPost("/logo/crypto/encrypt");
/** 生成图片验证码 */
export const kaptcha = axiosGet("/logo/sys/img-code/kaptcha");
/** 下载Excel导入模板 */
export const downloadExcelTemplate = axiosGet("/logo/easyPoi/downloadExcelTemplate");
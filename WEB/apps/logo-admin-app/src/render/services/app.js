import { axiosPost, axiosGet } from "../utils/request";

/** 登录接口 */
export const login = ({userName}) => {
  if (userName == 'admin') {
    console.log("登录成功")
    return Promise.resolve({
      code: 200,
      msg: "登录成功",
      data: {
        user: {name: "admin"},
      }
    })
  } else {
    console.log("登录失败")
    return Promise.reject()
  }
}
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
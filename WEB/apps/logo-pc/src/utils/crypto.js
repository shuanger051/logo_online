import { commonService } from "../apis";
import { runPromiseInSequence } from "./util";

// 获取公钥
function getPublicKey(ctx) {
  return commonService
    .getPublicKey()
    .then((res) => (ctx.publicKey = res.data.publicKey));
}

function encrypt(ctx) {
  return (
    commonService
      .encrypt({
        param: ctx.param,
        publicKey: ctx.publicKey,
      })
      // 返回密文
      .then((res) => res.data.sign)
  );
}

/**
 * == rsa 接口加密 ==
 * @param {*} param
 * @returns
 */
export function rsaEncrypt(param) {
  return runPromiseInSequence([getPublicKey, encrypt])({ param });
}

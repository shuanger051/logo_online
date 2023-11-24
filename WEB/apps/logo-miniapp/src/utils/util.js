


// 获取当前环境
export const getEnvByUa = () => {
  let tempEnv = sessionStorage.getItem('curUaEnv')
  if (!tempEnv) {
    const sUserAgent = window.navigator.userAgent.toLowerCase()
    const bIsDtDreamApp = sUserAgent.includes('dtdreamweb')
    const bIsAlipayMini = sUserAgent.includes('miniprogram') && sUserAgent.includes('alipay')
    const bIsWechatMini = sUserAgent.includes('miniprogram') && (sUserAgent.includes('wechat') || sUserAgent.includes('wx'))
    const oldWechat = sUserAgent.includes('micromessenger') // 微信兼容
    tempEnv = (bIsDtDreamApp && 'bIsDtDreamApp') || (bIsAlipayMini  && 'bIsAlipayMini') || ((bIsWechatMini || oldWechat) && WECHAT_ENV) || 'h5'
    // tempEnv = bIsDtDreamApp ? 'bIsDtDreamApp' : bIsAlipayMini ? 'bIsAlipayMini' : 'h5'
    sessionStorage.setItem('curUaEnv', tempEnv)
  }
  return tempEnv
}

/**
 * 顺序执行promise
 * @param {*} funcs
 * @returns
 */
export function runPromiseInSequence(funcs) {
  return (ctx = {}) => {
    function next(data) {
      const fn = funcs.shift();
      if (fn) {
        try {
          let ret = fn(ctx);
          // promise
          if (ret && ret.then) return ret.then(next);
          // sync
          else return next(ret);
        } catch (err) {
          return Promise.reject(err);
        }
      }
      // 队列函数执行完毕
      else return Promise.resolve(data);
    }
    return next();
  };
}
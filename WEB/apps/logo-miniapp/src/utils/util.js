
/**
 *  获取地址栏值
 * @param {string} name
 */
export const getQueryString = (name) => {
  const reg = new RegExp('(^|&|\\?)' + name + '=([^&]*)(&|$)', 'i')
  const r = window.location.href.match(reg)
  if (r != null) {
    return decodeURI(r[2])
  }
  return null
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
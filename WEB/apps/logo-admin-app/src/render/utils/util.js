
export function isDef(v) {
  return v !== undefined && v !== null;
}

/**
 * Remove an item from an array.
 */
export function remove(arr, item) {
  if (arr.length) {
    const index = arr.indexOf(item);
    if (index > -1) {
      return arr.splice(index, 1);
    }
  }
}

export function isRegExp(v) {
  return _toString.call(v) === "[object RegExp]";
}


const _toString = Object.prototype.toString;

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

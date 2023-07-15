/**
 * 锁定执行器
 * @param {*} fn
 * @returns
 */
export const lockfn = (fn) => {
  let locked = false
  let done = () => { locked = false }

  return (...args) => {
    if (!locked) {
      locked = true
      fn.apply(null, args.concat(done))
    }
  }
}

export const withlock = (obj) => {
  let locked = false
  obj.islocked = () => {
    return locked
  }
  obj.dolocked = (fn) => {
    locked = true
    fn(obj)
    locked = false
  }
  return obj
}

/**
 * 添加取消
 * @param {*} obj
 * @param {*} handler
 * @returns
 */
const addCancel = (obj, handler) => {
  obj.cancel = () => {
    handler()
  }
  return obj
}

/**
 * 等待异步函数
 * @param {*} fn
 * @param {*} time
 * @returns
 */
export const waitfn = (fn, timeout = 3000) => {
  let success, failure
  let wait = new Promise((resolve, reject) => {
    success = resolve
    failure = reject
  })

  const handler = async () => {
    try {
      let res = await fn()
      if (res !== false) {
        return success(res)
      }
    } catch (e) {
      failure(e)
    }
    cancel()
  }
  let time = setTimeout(failure, timeout)
  let cancel = () => { clearTimeout(time); time = null }
  handler()

  return addCancel(wait, cancel)
}

/**
 * 延迟执行
 * @param {*} fn
 * @param {*} time
 * @returns
 */
export const laterfn = (fn, timeout) => {
  let time = null
  let later = new Promise((resolve, reject) => {
    time = setTimeout(() => {
      try {
        let ret = fn()
        resolve(ret)
      } catch (e) {
        reject(e)
      }
      cancel()
    }, timeout)
  })
  let cancel = () => { clearTimeout(time); time = null }
  return addCancel(later, cancel)
}

const noop = () => {}
/**
 * 顺序执行
 * @param {*} array
 * @param {*} iterator
 * @param {*} callback
 */
export const eachSeries = function (array, iterator, callback = noop) {
  let len = array.length
  let index = 0
  let sync = false

  const step = () => {
    iterator(array[index], index, done)
  }

  const done = (err, result) => {
    if (err) {
      callback(err)
    } else if (++index == len) {
      callback(null, result)
      // 同步防止堆栈溢出
    } else if (sync) {
      setTimeout(step, 0)
    } else {
      sync = true
      step()
    }
    sync = false
  }

  step()
}

/**
 * dispatch
 */

export const dispatch = (array) => () => {
  array.forEach((item) => item())
}

export const isDev = () => {
  try {
    return process.env.NODE_ENV === 'development'
  } catch (e) {}
  return false
}

import { laterfn } from '@h5Designer/utils'
/**
 * delay中间件
 */
export default (params) => {
  let __later

  return (next) => {
    if (params.delay) {
      if (__later) {
        return
      }
      __later = laterfn(() => {
        __later.cancel()
        __later = null
        next()
      }, params.delay * 1000)
    } else {
      next()
    }
  }
}

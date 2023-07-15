/**
 * 容差计算器
 * @param {*} options
 * @returns
 */
const toleranceMaker = function (options = {}) {
  let {
    tolerance = 5
  } = options

  return (target, source, callback) => {
    var len = target.length
    var dvalue, arr = [], flag

    source.some((val) => {
      for (var i = 0; i < len; i++) {
        dvalue = target[i] - val
        if (Math.abs(dvalue) <= tolerance) {
          flag = true
          return true
        }
      }
    })

    if (flag) {
      source.forEach((val) => {
        if (
          target.indexOf(val + dvalue) > -1
        ) {
          arr.push(val + dvalue)
        }
      })
    }
    callback(flag ? dvalue : null, arr)
  }
}

export {
  toleranceMaker
}

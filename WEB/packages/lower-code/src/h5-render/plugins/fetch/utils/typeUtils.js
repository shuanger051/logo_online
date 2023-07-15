var toString = Object.prototype.toString

// 类型判断：
export function isFormData (val) {
  return (typeof FormData !== 'undefined') && (val instanceof FormData)
}

// 类型判断：
export function isArrayBuffer (val) {
  return toString.call(val) === '[object ArrayBuffer]'
}

// 类型判断：
export function isArrayBufferView (val) {
  var result
  if ((typeof ArrayBuffer !== 'undefined') && (ArrayBuffer.isView)) {
    result = ArrayBuffer.isView(val)
  } else {
    result = (val) && (val.buffer) && (val.buffer instanceof ArrayBuffer);
  }
  return result
}

// 类型判断：
function checkBuffer (obj) {
  return !!obj.constructor && typeof obj.constructor.isBuffer === 'function' && obj.constructor.isBuffer(obj)
}

// 类型判断：
function checkSlowBuffer (obj) {
  return typeof obj.readFloatLE === 'function' && typeof obj.slice === 'function' && checkBuffer(obj.slice(0, 0))
}

// 类型判断：
export function isBuffer (obj) {
  return obj != null && (checkBuffer(obj) || checkSlowBuffer(obj) || !!obj._isBuffer)
}

// 类型判断：是否为对象
export function isObject (val) {
  return val !== null && typeof val === 'object'
}

// 类型判断：是否为函数
export function isFunction (val) {
  return toString.call(val) === '[object Function]'
}

// 类型判断：
export function isStream (val) {
  return isObject(val) && isFunction(val.pipe)
}

// 类型判断：
export function isFile (val) {
  return toString.call(val) === '[object File]'
}

// 类型判断：
export function isBlob (val) {
  return toString.call(val) === '[object Blob]'
}

// 类型判断：
export function isMultiObject(val) {
  if (
    isFormData(val) ||
    isArrayBuffer(val) ||
    isBuffer(val) ||
    isStream(val) ||
    isFile(val) ||
    isBlob(val)
  ) {
    return true
  }
}

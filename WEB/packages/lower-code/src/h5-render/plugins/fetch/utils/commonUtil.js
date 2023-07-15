import Vue from 'vue'
import fetchApi from '../index'

const REBLOCK = /^\s+$/

const predict = (date) => {
  if (typeof date === 'string' && REBLOCK.test(date)) {
    return true
  } else {
    return false
  }
}

const arrOrObjStep = (resultFn, mapFn, date) => {
  if (date !== null && typeof date === 'object') {
    for (let i in date) {
      date[i] = mapFn(date[i])
    }
  } else {
    return resultFn(date)
  }
  return date
}

const createPostDepth = (step) => {
  return function postDepth (handler, date) {
    return step(handler, postDepth.bind(null, handler), date)
  }
}

/**
 *
 * @param {*} data
 * 递归去除字段中空格 " "--> ''
 */
export const blankFilter = (data) => {
  return createPostDepth(arrOrObjStep)((item) => {
    if (predict(item)) {
      return ''
    } else {
      return item
    }
  }, data)
}

/**
 * 数据库类型对象转驼峰型对象
 * @param obj 数据库类型对象
 * @param item 驼峰类型对象
 * @param type 转换类型（参数存在代表驼峰转成字段）
 * @return
 */
export function covertObj (obj, item, type) {
  let newObj = {}
  for (let key in obj) {
    let fieldName = columnName2FieldName(key)
    if (type) {
      fieldName = fieldName2ColumnName(key)
    }
    newObj[fieldName] = obj[key]
  }
  Object.assign(item, newObj)
}

export function covertFieldToString (obj) {
  for (let key in obj) {
    let val = obj[key] !== undefined ? obj[key] : ''
    obj[key] = val + ''
  }
}
/*
 * 判断是否是json格式
 */
export function isJSONStr (str) {
  if (typeof str === 'string') {
    try {
      var obj = JSON.parse(str)
      if (typeof obj === 'object' && obj) {
        return true
      } else {
        return false
      }
    } catch (e) {
      return false
    }
  }
}

/**
 * 把参数里的内容转义成html语言
 * @param obj 数据库类型对象
 * @return
 */
export function transferenceParam (obj, item) {
  let newObj = {}
  for (let key in obj) {
    let val = obj[key] ? obj[key] : ''
    if (val !== '') {
      val = covertHtml(val)
    }
    newObj[key] = val
  }
  Object.assign(item, newObj)
}
/**
 * 跨站脚本攻击，字符转义
 */
export function covertHtml (value) {
  if (!value) return ''
  let val = value + ''
  val = val.toLowerCase() // 转换成小写
  if (val === '' || val === 'undefined' || val === 'null') return ''
  let arrEntities = {'<': '&lt;', '>': '&gt;'}
  let rs = val.replace(/(<|>)/ig, (all, t) => {
    return arrEntities[t]
  })
  return rs
}
/**
 * 数据库列名转属性(下划线转驼峰)
 * @param columnName 数据库列名
 * @return
 */
function columnName2FieldName (columnName) {
  let result = ''
  let upcaseFlag = false
  if (!columnName || columnName === '') return columnName
  for (let i = 0; i < columnName.length; i++) {
    let ch = columnName[i]
    if (ch === '_') {
      upcaseFlag = true
      continue
    } else if (upcaseFlag) {
      result += ('' + ch).toUpperCase()
      upcaseFlag = false
    } else {
      result += ch
      upcaseFlag = false
    }
  }
  return result
}
/**
 * 属性转数据库列名(驼峰传下划线)
 * @param str 属性名
 * @return
 */
function fieldName2ColumnName (columnName) {
  let result = ''
  if (!columnName || columnName === '') return columnName
  for (let i = 0; i < columnName.length; i++) {
    let ch = columnName[i]
    if (allCaps(ch)) {
      result += ('_' + ch).toLowerCase()
    } else {
      result += ch
    }
  }
  return result
}
function allCaps (text) {
  if (text < 'A' || text > 'Z') {
    return false
  }
  return true
}

/**
 * 动态监听form查询表单高度变化来自适应表格高度
 * @param str 属性名
 * @return
 */
export function handlePanelChange (id, ref) {
  let MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver
  let mutationObserverSupport = !!MutationObserver
  if (mutationObserverSupport) {
    let observer = new MutationObserver((mutations) => {
      mutations.forEach((item) => {
        ref.selfAdaption()
      })
    })
    const options = {
      // 'childList' : true,//子节点的变动
      attributes: true, // 属性的变动
      // 'characterData' : true,//节点内容或节点文本的变动
      // 'subtree' : true,//所有后代节点的变动
      attributeOldValue: true // 表示观察attributes变动时，是否需要记录变动前的属性
      // 'characterDataOldValue' : true//表示观察characterData变动时，是否需要记录变动前的值
    }
    let document = window.document.getElementById(id)
    observer.observe(document, options)
  }
}
const isServer = Vue.prototype.$isServer
/* istanbul ignore next */
export const on = (function () {
  if (!isServer && document.addEventListener) {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false)
      }
    }
  } else {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.attachEvent('on' + event, handler)
      }
    }
  }
})()

/* istanbul ignore next */
export const off = (function () {
  if (!isServer && document.removeEventListener) {
    return function (element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false)
      }
    }
  } else {
    return function (element, event, handler) {
      if (element && event) {
        element.detachEvent('on' + event, handler)
      }
    }
  }
})()
export function IsInArray (arr, val) {
  let right = arr[val]
  if (right && right !== '') {
    return true
  } else {
    return false
  }
}
/**
 * 表格上移
 */
export function rowMoveUp (currentSelectList, currentSelectRowInx, tempInx, refs, moveUpDisabled, moveDownDisabled, _this) {
  if (currentSelectList && currentSelectList.length === 1) {
    _this.tempInx = currentSelectRowInx[0]
    for (let i = 0; i < refs.tData.length; i++) {
      refs.tData[i]._checked = false
    }
    _this.currentSelectList = []
    _this.currentSelectRowInx = []
    if (_this.tempInx > 0) {
      let upDate = refs.tData[_this.tempInx -1]
      refs.tData.splice(_this.tempInx -1,1)
      refs.tData.splice(_this.tempInx,0,upDate)

      refs.tData[_this.tempInx -1]._checked = true
      _this.currentSelectList.push(refs.tData[_this.tempInx -1])
      _this.currentSelectRowInx.push(_this.tempInx -1)
      _this.moveDownDisabled = false
      if (_this.tempInx === 1) {
        _this.moveUpDisabled = true
      }
    } else {
      refs.tData[_this.tempInx]._checked = true
      _this.currentSelectList.push(refs.tData[_this.tempInx])
      _this.currentSelectRowInx.push(_this.tempInx)
      _this.$hMessage.info(_this.$t('m.i.common.moveMsg_1'))
    }
  } else {
    _this.$hMessage.info(_this.$t('m.i.common.chooseOneData'))
  }
}
/**
 * 表格下移
 */
export function rowMoveDown (currentSelectList, currentSelectRowInx, tempInx, refs, moveUpDisabled, moveDownDisabled, _this) {
  if (currentSelectList && currentSelectList.length === 1) {
    _this.tempInx = _this.currentSelectRowInx[0]
    if ((_this.tempInx + 1) === refs.tData.length) {
      _this.$hMessage.info(_this.$t('m.i.common.moveMsg_2'))
    } else {
      for (let i = 0; i < refs.tData.length; i++) {
        refs.tData[i]._checked = false
      }
      _this.moveUpDisabled = false
      let downDate = refs.tData[_this.tempInx + 1]
      refs.tData.splice(_this.tempInx + 1, 1)
      refs.tData.splice(_this.tempInx, 0, downDate)

      refs.tData[_this.tempInx +1]._checked = true
      _this.currentSelectRowInx = []
      _this.currentSelectRowInx.push(_this.tempInx + 1)
      if ((_this.tempInx + 1) === refs.tData.length - 1) {
        _this.moveDownDisabled = true
      }
    }
  } else {
    _this.$hMessage.info(_this.$t('m.i.common.chooseOneData'))
  }
}

/**
 * post方式发送请求方法
 * @param {*参数} params
 * @param {*请求url} url
 */
export function post (params, url) {
  let cig = window.LOCAL_CONFIG || window.FRAME_CONFIG
  let isUrlLowercase = cig.isUrlLowercase + ''
  if (isUrlLowercase !== 'false') {
    url = url.toLowerCase()
  }
  return fetchApi.fetch.post(url, params)
}

/**
 * 时间格式化
 */
export function dateFormat (dateInt, timeInt, hasTime, format) {
  if (!format) {
    format = '-'
  }
  let dateStr = dateInt + ''
  let timeStr = timeInt + ''
  let y = dateStr.substring(0, 4) + format
  let m = dateStr.substring(4, 6) + format
  let d = dateStr.substring(6, 8)
  if (dateStr.length !== 8) {
    // 日期不足8位或超出8位，直接返回空
    return ''
  }
  if (!hasTime) {
    // 没有时间，只有日期
    return `${y}${m}${d}`
  } else {
    if (timeStr.length === 6) {
      // 10:10:10
      let hh = timeStr.substring(0, 2)
      let mm = timeStr.substring(2, 4)
      let ss = timeStr.substring(4, 6)
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    } else if (timeStr.length === 5) {
      // 09:10:10
      let hh = timeStr.substring(0, 1).padStart(2, '0')
      let mm = timeStr.substring(1, 3)
      let ss = timeStr.substring(3, 5)
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    } else if (timeStr.length === 4) {
      // 00:10:10
      let hh = '00'
      let mm = timeStr.substring(0, 2)
      let ss = timeStr.substring(2, 4)
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    } else if (timeStr.length === 3) {
      // 00:01:10
      let hh = '00'
      let mm = timeStr.substring(0, 1).padStart(2, '0')
      let ss = timeStr.substring(1, 3)
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    } else if (timeStr.length === 2) {
      // 00:00:10
      let hh = '00'
      let mm = '00'
      let ss = timeStr.substring(0, 2)
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    } else if (timeStr.length === 1) {
      // 00:00:09
      let hh = '00'
      let mm = '00'
      let ss = timeStr.substring(0, 1).padStart(2, '0')
      return `${y}${m}${d} ${hh}:${mm}:${ss}`
    }
  }
}

function broadcast(componentName, eventName, params) {
  this.$children.forEach(child => {
    const name = child.$options.name

    if (name === componentName) {
      child.$emit.apply(child, [eventName].concat(params))
    } else {
      broadcast.apply(child, [componentName, eventName].concat([params]))
    }
  })
}

export const emitter = {
  methods: {
    dispatch(componentName, eventName, params) {
      let parent = this.$parent || this.$root
      let name = parent.$options.name

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent

        if (parent) {
          name = parent.$options.name
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params))
      }
    },
    broadcast(componentName, eventName, params) {
      broadcast.call(this, componentName, eventName, params)
    }
  }
}



export const createJsonFetch  = function () {
  const config = {
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  }
  let fetch = fetchApi.createFetch(config)
  fetchApi.addFetchInterceptors(fetch, config, false)
  return fetch
}

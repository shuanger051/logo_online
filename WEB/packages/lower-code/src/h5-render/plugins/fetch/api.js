import axios from 'axios'
import config from './_api_config'
import { Toast } from 'vant'
import 'vant/lib/toast/style'
import { isJSONStr, covertObj, blankFilter, covertFieldToString } from './utils/commonUtil'
import {isMultiObject} from './utils/typeUtils'
import * as inface from './interface'

export const createFetch = (params = {}) => {
  params = Object.assign({}, config, params)
  const fetch = axios.create(params)

  fetch.all = axios.all.bind(fetch)
  fetch.spread = axios.spread.bind(fetch)
  return fetch
}

export const addFetchInterceptors = (fetch = {}, params = {}, isFieldToString = true) => {
  fetch.interceptors.response.use(response => {
    let cig = window.LOCAL_CONFIG || window.FRAME_CONFIG || {}

    // ie中res.data返回为string类型，chorme为obj类型
    if (response.data && typeof response.data === 'string') {
      if (isJSONStr(response.data)) {
        response.data = JSON.parse(response.data)
      }
    }

    if (response.data && response.data.data) {
      response._transdata = response.data.data
    }

    // 为了兼容老的业务系统中的返回值
    if (response.data && response.data.data) {
      response.data = response.data.data.length == 1 ? response.data.data[0] : response.data.data
      response._isTransdata = true
    }

    // 当返回对象有data时， 去除data里面的空格 ' ' --> ''
    if (!params.notIgnoreBlank) {
      let { data } = response
      if (data && !isMultiObject(data)) {
        response.data = blankFilter(data)
      }
    }
    let isIARCasLogin = cig.isIARCasLogin + ''
    let isCasAdapter = cig.isCasAdapter + ''
    if (isIARCasLogin == 'true') {
      if (response.status + '' !== '200') {
        if (response.request.responseURL.indexOf('cas') > 0) {
          window.location.href = response.request.responseURL
        } else {
          return response
        }
      } else if (response.request.responseURL.indexOf('cas') > 0) {
        window.sessionStorage.removeItem('menus')
        // 跳转到cas登录页
        window.location.href = response.request.responseURL
      }
    }

    if (isCasAdapter == 'true') {
      if (response.status + '' !== '200') {
        if (response.request.responseURL.indexOf('/uias/') > 0) {
          window.location.href = response.request.responseURL
        } else {
          return response
        }
      } else if (response.request.responseURL.indexOf('/uias/') > 0) {
        window.sessionStorage.removeItem('menus')
        // 跳转到cas登录页
        window.location.href = response.request.responseURL
      }
    }
    return response
  }, error => {
    if (error && error.response && error.response.data && error.response.data.data) { // 兼容IAR模式 20180915 add by 周智星
      error.response.data = error.response.data.data.length === 1 ? error.response.data.data[0] : error.response.data.data
    }
    if (error && error.response && error.response.data && error.response.data.return_code) {
      let flag = true
      let text = ''
      let return_code = error.response.data.return_code
      if (return_code === 9008) {
        text = '对不起,您缺少访问权限'
      } else if (return_code === 9006) {
        text = '页面已经失效,请先登录'
        flag = false
      } else if (return_code === 9007) {
        text = '会话已失效,请重新登录'
        flag = false
      } else if (return_code === 9004) {
        text = '您已被踢出,请先登录'
        flag = false
      } else if (return_code === 9005) {
        text = '您已在别地方登录,请先登录'
        flag = false
      } else if (return_code === 9011) {
        text = '验证码出错'
      } else {
        flag = false
        text = '当前服务端登录验证出错,请重新登录'
      }
      if (!flag) {
        Toast({
          message: text,
          onClose: () => {
            inface.logout()
          }
        })
      } else {
        if (text !== '') {
          Toast(text)
        }
      }
    }
    if (error && error.response && error.response.data && error.response.data.error_no) {
      let text = error.response.data.error_info
      if (error.response.data.error_no !== 96000) {
        if (text.trim() !== '') {
          Toast.fail('操作失败，' + text)
        } else {
          Toast.fail('操作失败！')
        }
      }
    } else {
      Toast.fail('未授权或网络通信失败！')
    }
    return Promise.reject(error)
  })
}
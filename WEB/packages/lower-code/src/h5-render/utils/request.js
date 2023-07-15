import api from '../plugins/fetch'
import { Toast } from 'vant'
import 'vant/lib/toast/style'
import { ismPaaSOS, requestByRPC } from '@winner-fed/mpaas-jsapi'
import { removePermanentDataFunc } from '@h5Render/utils/index'
export let fetchApi = api.fetch

window.CMS_CONFIG = window.CMS_CONFIG || {}

export let baseCCMUrl = window.CMS_CONFIG.CCM_URL // 内容中心
export let baseOMSUrl = window.CMS_CONFIG.OMS_URL // 财富oms
export let baseOMCUrl = window.CMS_CONFIG.OMC_URL // 操作员中心
export let baseWFSUrl = window.CMS_CONFIG.WFS_URL // 工作流
export let baseACMUrl = window.CMS_CONFIG.ACM_URL // 活动中心
export let baseMMSURL = window.CMS_CONFIG.MMS_URL // 物料中心

export let isMPaasPackage = window.CMS_CONFIG.IS_MPAAS_PACKAGE // 是否是mpaas环境
export let AppId = window.CMS_CONFIG.MPAAS_APPID // 是否是mpaas环境
export let WorkspaceId = window.CMS_CONFIG.MPAAS_WORKSPACEID // 是否是mpaas环境
export let Version = window.CMS_CONFIG.MPAAS_VERSION // 是否是mpaas环境

export let baseMUrl = 'https://mgw.mpaas.cn-hangzhou.aliyuncs.com/mgw.htm'
/**
 * 包含mPass方式发送请求方法
 * @param {*} params 
 * @param {*} url 
 * @param {*} config 
 * @returns 
 */
 export const apiPost = (params = {}, url, showError = true) => {
    console.log(isMPaasPackage) 
    if (isMPaasPackage === 'true') {
        // 1.1、mPaas，站内包
      if (ismPaaSOS()) {
        console.log('ismPaaSOS')
        return new Promise((resolve, reject) => {
          // requestByRPC入参url、data、headers、method、callback
          // todo 这里目前缺少接口返回错误信息时的判断，错误处理目前都在具体接口调用处
          // callback回调里后面改为获取数据后，再判断resolve或者reject
          let data = params
          // data.org_code = window.LOCAL_CONFIG.ORG_CODE
          console.log(data)
          requestByRPC({
            url,
            method: 'post',
            data,
            callback: (result) => {
              console.log(result)
              if (result.error != '1000') {
                // mpaas返回非1000，1000为处理正常
                if (result.error == '1005' || result.error == '2000') {
                  // 1005、2000用户登录信息失效，mPass是长效缓存所以过期要清空缓存
                  removePermanentDataFunc('userInfo', true)
                  removePermanentDataFunc('mpcUserInfo', true)
                }
                // 服务器错误
                resolve({
                  error_info: result.errorMessage,
                  data: result
                })
              } else {
                // mpaas返回1000
                if (result.error_info && result.error_no != '0') {
                  // 接口非200
                  const res = !result.data ? { data: result } : result; // 兼容本来就存在data的接口
                  // 20100用户登录信息失效，mPass是长效缓存所以过期要清空缓存
                  if (result.error_no == '20100') {
                    removePermanentDataFunc('userInfo', true)
                    removePermanentDataFunc('mpcUserInfo', true)
                  }
                  reject(res)
                } else {
                  // 正常200等
                  const res = !result.data ? { data: result } : result; // 兼容本来就存在data的接口
                  resolve(res)
                }
              }
            }
          })
        })
      } else {
        console.log('unmPaaSOS')
        // 1.2、mpaas，站外包
        return _apiPost('mPaas', params, url, showError)
      }
    } else {
      return _apiPost('nromal', params, url, showError)
    }
 }

/**
 * post方式发送请求方法
 * @param {*参数} params
 * @param {*请求url} url
 */
export function post(type, params, url, config = {}) {
  console.log('type:', type)
  let isUrlLowercase = window.CMS_CONFIG.isUrlLowercase + ''
  if (isUrlLowercase === 'true') {
    url = url.toLowerCase()
  }
  const operator_code = window.sessionStorage.getItem('operator_code') || window.sessionStorage.getItem('currUserId') || 'admin'
  if (type === 'nromal') {
    return fetchApi.post(url, { ...params, operator_code }, config)
  } else {
    let operation_type = url.split('/m/')[1]
    config.headers = {
      'Content-Type': 'application/json',
      'Operation-Type': operation_type,
      'AppId': AppId,
      'WorkspaceId': WorkspaceId,
      'Version': Version,
      [`X-CORS-${AppId}-${WorkspaceId}`]: '1'
    }
    const requestData = [{"_requestBody": {...params, operator_code} }]
    url = url.split('/m/')[0]
    return fetchApi.post(url, requestData, config)
  }
}

/**
 * 对接口错误进行统一，正确直接返回整个res
 * 具体到错误处理，在对应业务中用.catch去捕获
 */
export const _apiPost = async (type, params, url, showError) => {
  if (Object.prototype.toString.call(params) !== '[object Object]') {
    url = params
    params = {}
  }
  // 未获取到用户信息
  // if (!fullParams.oper_id) {
  //   Toast({
  //     message: '未获取到用户信息，请退出重新登录',
  //     duration: 10
  //   })
  // }
  const res = await post(type, params, url)
  return new Promise((resolve, reject) => {
    if (res) {
      if (type !== 'nromal' && res.headers['result-status'] && res.headers['result-status'] != '1000') {
        if (res.headers['result-status'] == '1005' || res.headers['result-status'] == '2000') {
          // 1005、2000用户登录信息失效，清除sessionStorage缓存
          removePermanentDataFunc('userInfo')
          removePermanentDataFunc('mpcUserInfo')
        }
        const responseData = {
          error_code: String(res.headers['result-status']),
          error_info: res.headers.tips ? decodeURIComponent(res.headers.tips) : '',
          error_no: res.headers['result-status']
        };
        reject(responseData);
      } else if (!res.data.error_info) {
        resolve(res)
      } else {
        if (showError) {
          Toast.fail({
            message: res.data.error_info,
            duration: 3000
          })
        }
        console.log(res, 'apiPost')
        reject(res)
      }
    }
    reject(res)
  })
}


export const resolveArr = (resolve, res) => {
  if (Array.isArray(res.data)) {
    resolve(res)
  } else {
    res.data = [res.data]
    resolve(res)
  }
}

/**
 * 文件上传
 */
export function uploadFiles(url, form) {
  return fetchApi.post(url, form, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 文件下载
 */
export function downloadFiles(url, params = {}, config = { responseType: 'blob' }) {
  return fetchApi.post(url, params, config).then(res => {
    let reader = new FileReader() // 创建读取文件对象
    reader.readAsText(res.data, 'utf-8')
    reader.onload = function(e) {
      const result = e.target.result
      if (res.data.type === 'application/json') {
        let jsonData = JSON.parse(result)
        if (jsonData.error_info) {
          Toast.fail({
            message: jsonData.error_info,
            duration: 5
          })
        }
      } else {
        // 线上不跨域的情况，或者开启Access-Control-Expose-Headers: Content-Disposition
        const disposition = res.headers['content-disposition']
        let fileName = ''
        if (process.env.NODE_ENV === 'development') {
          fileName = '自定义的名称.xls'
        } else {
          fileName = decodeURI(disposition.substring(disposition.indexOf('filename=') + 9, disposition.length))
        }
        if (window.navigator.msSaveOrOpenBlob) {
          const blob = new Blob([res.data])
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          let href = window.URL.createObjectURL(res.data)
          link.href = href
          link.download = fileName
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(href)
        }
      }
    }
  })
}

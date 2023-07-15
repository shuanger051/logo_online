import Qs from 'qs'
import * as utils from './utils/typeUtils'

export default{
  // 请求的接口，在请求的时候，如axios.get(url,config);这里的url会覆盖掉config中的url
  url: '/user',
  // 请求的接口
  // baseURL: (window.LOCAL_CONFIG||window.FRAME_CONFIG).API_HOME || 'http://127.0.0.1:8080/server',
  // 默认请求方法
  title: '13',
  method: 'get',
  // 在发送请求之前对请求数据做处理
  transformRequest: [
    function (data, headers) {
      if (utils.isMultiObject(data)) {
        return data
      }

      let contentType = headers['Content-Type'] || ''
      let isObject = utils.isObject(data)

      if (
        ~contentType.indexOf('application/x-www-form-urlencoded') &&
        isObject
      ) {
        return Qs.stringify(data)
      }

      if (
        ~contentType.indexOf('application/json') &&
        isObject
      ) {
        return JSON.stringify(data)
      }

      return data
    }
  ],

  // 提前处理返回的数据=
  transformResponse: [
    function (data) {
      return data
    }
  ],
  // 请求头信息
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  // 默认parameter参数
  params: {
  },

  // 序列化param
  paramsSerializer: function (params) {
    return Qs.stringify(params)
  },

  // 设置超时时间
  timeout: 500000000,

  // 是否跨站点访问控制请求
  // withCredentials: false, // default
  withCredentials: false, // 自动携带cookie

  // 返回数据类型
  responseType: 'json', // default

  // 将upload事件注释掉，防止跨域状态下发起option请求

  // onUploadProgress: function(progressEvent) {
  //   // Do whatever you want with the native progress event
  // },
  // onDownloadProgress: function(progressEvent) {
  //   // Do whatever you want with the native progress event
  // },
  maxContentLength: 2000,
  validateStatus: function (status) {
    return status >= 200 && status < 300 // default
  },
  // `maxRedirects` 定义在 node.js 中 follow 的最大重定向数目
  // 如果设置为0，将不会 follow 任何重定向
  maxRedirects: 5
}

import axios from 'axios'
import api from 'ucp-components/lib/utils/ucpFetch'
import hMessage from 'h_ui/dist/lib/Message'

let fetchApi = api.fetch
export const baseParams = {
  operator_code: window.sessionStorage.getItem('userName') || window.sessionStorage.getItem('currUserId')
}

/**
 * 对接口错误进行统一，正确直接返回整个res
 * 具体到错误处理，在对应业务中用.catch去捕获
 */
let CancelToken = axios.CancelToken
export const apiPost = async (params = {}, url, btn) => {
  if (Object.prototype.toString.call(params) !== '[object Object]') {
    url = params
    params = {}
  }
  const res = await fetchApi.post(
    url,
    {
      ...baseParams,
      ...params
    },
    {
      cancelToken: new CancelToken(c => {
        if (btn) {
          btn.handleCancel = c
        }
      })
    })
  return new Promise((resolve, reject) => {
    if (!res.data) return resolve(res)
    if (!res.data.error_info) {
      resolve(res)
    } else {
      hMessage.error({
        content: res.data.error_info,
        duration: 1
      })
      reject(res)
    }
  })
}

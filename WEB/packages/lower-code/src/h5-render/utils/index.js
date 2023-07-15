
import { baseOMSUrl } from '@Utils/request'
import { ismPaaSOS, setStorage, getStorage, removeStorage, startApp } from '@winner-fed/mpaas-jsapi'

/**
 * 获取URL参数
 * @param name 需要获取的参数key
 * @return
 */
 export function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  var r = decodeURI(window.location.search.substr(1)).match(reg)
  if (r != null) return unescape(r[2])
  return null
}

/**
 * 链接拼接
 * @param url 图片链接
 * @return
 */
export function formatImgUrlFunc(url) {
  if (url) {
    if (url.indexOf('http') > -1) {
      return url
    } if (url.indexOf('previewFilePlt') > -1) {
      const imgUrl = baseOMSUrl ? baseOMSUrl + url : url
      return imgUrl
    } else {
      const imgUrl = window.CMS_CONFIG.IMAGE_URL ? window.CMS_CONFIG.IMAGE_URL + url : url
      return imgUrl
    }
  } else {
    return ''
  }
}

/**
 * 存储数据
 * @param key
 * @param value
 * @returns {string}
 */
export function setPermanentDataFunc(key, value, isAppStorage = false) {
  if (ismPaaSOS() && isAppStorage) {
    setStorage({key, value})
  } else {
    window.sessionStorage.setItem(key, value)
  }
}

/**
 * 获取数据
 * @param key
 * @returns {string}
 */
export function getPermanentDataFunc(key, isAppStorage = false) {
  if (isAppStorage) {
    if (ismPaaSOS()) {
      return new Promise((resolve) => {
        getStorage(key, (res) => {
          console.log('获取mPaaS缓存信息', res);
          if (res && res.data) {
            const data = res.data
            resolve(data)
          } else {
            resolve('{}');
          }
        });
      });
    } else {
      return new Promise((resolve) => {
        const value = window.sessionStorage.getItem(key)
        resolve(value)
      });
    }
  } else {
    const value = window.sessionStorage.getItem(key)
    return value
  }
}

/**
 * 清除存储数据
 * @param key
 * @param value
 * @returns {string}
 */
export function removePermanentDataFunc(key, isAppStorage = false) {
  if (ismPaaSOS() && isAppStorage) {
    removeStorage(key)
  } else {
    window.sessionStorage.removeItem(key)
  }
}

/**
 * 拼接登录中转页链接
 * @param loginReq  登录要求
 * @returns {string}
 */
export function getLoginUrlFunc(loginReq = 'mobileLogin', loginUrl = '') {
  const localUrl = window.location.href
  const params = localUrl.split('?')[1] || ''
  const works_url = `${window.location.origin}${window.location.pathname}?${params}`
  let url = ''
  if (loginUrl.indexOf('?') > -1) {
    url = `${loginUrl}&loginReq=${loginReq}&callbackUrl=${encodeURIComponent(works_url)}`
    window.location.replace(url)
  } else {
    if (ismPaaSOS()) {
      const loginUrl = loginUrl || ''
      const appId = loginUrl.substring(7,15)
      const url = loginUrl.slice(15) + `?loginReq=${loginReq}&callbackUrl=${encodeURIComponent(works_url)}`
      const params = {
        appId: appId,
        param: {
          url: url
        }
      };
      startApp(params);
    } else {
      url = `${loginUrl}/?loginReq=${loginReq}&callbackUrl=${encodeURIComponent(works_url)}`
      window.location.replace(url)
    }
  }
}

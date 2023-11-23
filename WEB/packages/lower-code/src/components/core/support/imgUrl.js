import appStore from '@/store'

export const resolveImgUrl = (url, flag=false) => {
  let isDev = process.env.NODE_ENV === "development";
  let prefix = process.env.VUE_APP_API_BASE_URL;
  const reg = /^(http|\/\/:|\/static)/
  if (reg.test(url)) {
    return url
  }
  // return isDev ? '/api/logo'+url : prefix + '/logo/'+url
  const str = `${window.__baseUrl}/api/logo`+url
  if (flag) {
    return addQuery(str)
  } 
  return str
}

export const addQuery = (url) => {
  let isApp = window.$editorConfig.isApp()
  if (isApp) {
    url+= (url.includes('?') ? '&':'?') + '&token='+appStore.user.token
  }
  return url
}
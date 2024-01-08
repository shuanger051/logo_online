// import appStore from '@/store'
import {appGetMaterial, getMaterialListByPageOSS} from 'core/api'
import { convertImageToBase64} from "@editor/utils/canvas-helper.js";

export const resolveImgUrl = (url, flag = false) => {

  const reg = /^(http|\/\/:|\/static)/;
  if (reg.test(url)) {
    return url;
  }
  const str = [window.__baseUrl, "logo", url].join("");
  if (flag) {
    return addQuery(str);
  }
  return str;
};

export const addQuery = (url) => {
  let isAddToken = window.$editorConfig.mode!=='admin';
  const token = sessionStorage.getItem("token");
  if (isAddToken) {
    url += (url.includes("?") ? "&" : "?") + "token=" + token;
  }
  return url;
};

const getRealUrl = async (url) => {
  const lists = url.split('/')
  const name = lists[lists.length-1]
  const api = window.$editorConfig.mode !== 'admin' ? appGetMaterial: getMaterialListByPageOSS
  const data = await api({
    fileName: name,
    fileType: 1
  })
  const item = data.data.list[0]
  return item?item.urlPath : ''
  
}

export const resolveImgUrlBase64 = async (url, flag=true)=> {
  const reg = /img-save-dir.oss|\/static/;
  let rurl = url
  // if (!reg.test(url)) {
  //   rurl = await getRealUrl(url)
  // }
  rurl = resolveImgUrl(rurl, true)


  if (!flag) {
    return rurl
  } 
  let ps = new Promise((r,rj) => {
    convertImageToBase64(rurl, (src)=>{
      r(src)
    })
  })
  return ps
}
// import appStore from '@/store'
import {
  appGetMaterialListByPageApiOSS,
  getMaterialListByPageOSS,
} from "core/api";
import { convertImageToBase64 } from "@editor/utils/canvas-helper.js";

export const resolveImgUrl = (url, flag = false) => {
  const reg = /^(http|\/\/:|\/static)/;
  if (reg.test(url)) {
    return url;
  }
  return url;
};

export const addQuery = (url) => {
  const reg = /img-save-dir\.oss|\/static/;

  let addQeury = window.$editorConfig.mode!=='admin' && !reg.test(url);
  const token = sessionStorage.getItem("token");
  if (addQeury) {
    url += (url.includes("?") ? "&" : "?") + "token=" + token;
  }
  return url;
};

export const picCache = new Map()


const getRealUrl = async (url) => {
  let cache
  if (cache = picCache.get(url)) {
    return cache
  }
  const lists = url.split('/')
  const name = lists[lists.length-1]
  const api = window.$editorConfig.mode !== 'admin' ? appGetMaterialListByPageApiOSS: getMaterialListByPageOSS
  const data = await api({
    fileName: name,
    fileType: 1
  })
  const item = data.data.list[0]
  if (item.urlPath) {
    picCache.set(url, item.urlPath)
  }
  return item?item.urlPath : ''
  
}

export const resolveImgUrlBase64 = async (url, flag=true, callback=() => {})=> {
  const reg = /img-save-dir\.oss|\/static/;
  let rurl = url
  if (!reg.test(rurl)) {
      rurl = await getRealUrl(url)
  }
  rurl = resolveImgUrl(rurl, true)
  if (!flag) {
    return rurl
  } 
  let ps = new Promise((r,rj) => {
    convertImageToBase64(rurl, (src)=>{
      r(src)
      callback(src)
    })
  })
  return ps
}
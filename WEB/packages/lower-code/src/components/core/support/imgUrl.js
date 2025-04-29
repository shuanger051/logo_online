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


export const resolveImgUrlBase64 = async (url, flag=true, callback=() => {})=> {
  let rurl = url
  rurl = resolveImgUrl(rurl, true)
  if (!flag) {
    callback(rurl)
    return rurl
  } 
  let ps = new Promise((r,rj) => {
    convertImageToBase64(rurl, (src, w, h)=>{
      r(src)
      callback(src, w,h)
    })
  })
  return ps
}

export const resolveImgUrlBase64RetWH =  async (url, flag=true)=> {
  return new Promise((r) => {
    resolveImgUrlBase64(url, flag, (src, w, h) => {
        r({
          src,
          w, 
          h
        })
    })
  }) 
} 
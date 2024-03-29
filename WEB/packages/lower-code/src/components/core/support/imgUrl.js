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
  const str = [window.__baseUrl, "logo", url].join("");
  if (flag) {
    // return addQuery(str);
  }
  return str;
};

export const addQuery = (url) => {
  let isApp = window.$editorConfig.isApp();
  const token = sessionStorage.getItem("token");
  if (isApp) {
    url += (url.includes("?") ? "&" : "?") + "token=" + token;
  }
  return url;
};

const getRealUrl = async (url) => {
  const lists = url.split("/");
  const name = lists[lists.length - 1];

  let data;
  if (window.$editorConfig.mode !== "admin") {
    data = await appGetMaterialListByPageApiOSS({
      fileName: name,
    });
  } else {
    data = await getMaterialListByPageOSS({
      fileName: name,
      fileType: 1,
    });
  }

  const item = data.data.list[0];
  console.log(item ? item.urlPath : "",99999)
  return item ? item.urlPath : "";
};

export const resolveImgUrlBase64 = async (url, flag = true) => {
  const reg = /img-save-dir.oss|\/static/;
  let rurl = url;
  if (!reg.test(url)) {
    rurl = await getRealUrl(url);
  }
  if (!flag) {
    return rurl;
  }
  let ps = new Promise((r, rj) => {
    convertImageToBase64(rurl, (src) => {
      r(src);
    });
  });
  return ps;
};

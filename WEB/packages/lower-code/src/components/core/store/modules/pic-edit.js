import { takeScreenshot} from "@editor/utils/canvas-helper.js";
import {appUploadMaterialAttachmentBase64APIOSS} from "core/api"

export const actions = {
  async mCreateCover({}, {el}) {

      const base64 = await takeScreenshot({selector: el, type: 'dataUrl'});

      return await appUploadMaterialAttachmentBase64APIOSS({
        base64
      })
  },
  setCurrentWorkData({commit}, payload) {
    commit('setCurrentWorkData', payload)
  },
  setPic({commit}, payload) {
    commit('setPic', payload)
  },
  changeTokenScreenShotStatus({commit}, payload = true) {
    commit('changeTokenScreenShotStatus', payload)
  }
}
export const mutations = {

  setCurrentWorkData(state, payload) {
    state.mobile.currentWorkData = payload;
  },
  setPic(state, payload) {
    state[payload.type] = payload.value
  },
  changeTokenScreenShotStatus(state, payload) {
    state.tokenScreenShotStatus = payload
  }
}

export const state = {
  mobile: {
    currentWorkData: null,
  },
  tokenScreenShotStatus: false,
  // 店招图
  signboardPic: null,
  // 实景图
  livePic: null,
  // 店招实景合成图
  composePic: null,
  // 店招图base64,
  
  
}

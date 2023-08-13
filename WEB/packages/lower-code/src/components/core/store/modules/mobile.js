import { takeScreenshot,downloadPoster} from "@editor/utils/canvas-helper.js";
import {appSaveLogoInfoAPI} from "core/api"
export const actions = {
  async mCreateCover({ commit, state, dispatch,rootState }, {el}) {
      commit('setEditingElement', null)
      const file = await takeScreenshot({selector: el});
      const form = new FormData()
      form.append('file', file)
      form.append('shopsId', 10)
      form.append('merchantId', 6)
      const res = await appSaveLogoInfoAPI(form)
      commit('setShopSign', res.data.urlPath)
  },
  setLivePic({commit}, payload) {
    commit('setLivePic', payload)
  }
}
export const mutations = {

  setShopSign(state, payload) {
    state.mobile.currentShopSign = payload;
  },
  setLivePic(state, payload) {
    state.mobile.currentLivePic = payload
  }
}

export const state = {
  mobile: {
    currentShopSign: '',
    currentLivePic: null
  }
}


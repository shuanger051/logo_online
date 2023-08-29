import { takeScreenshot,downloadPoster} from "@editor/utils/canvas-helper.js";
import {appSaveLogoInfoAPI, appUploadShopsAttachmentAPI} from "core/api"
import router from '@/router'
import appStore from '@/store'
export const actions = {
  async mCreateCover({ commit, state, dispatch,rootState }, {el, file}) {
      commit('setEditingElement', null)
      let shopsId = router.currentRoute.query.shopId;
      let merchantId =  appStore.state.user.merchant.id
      let fileData = file
      if (!fileData) {
        fileData = await takeScreenshot({selector: el});
      } 
      const form = new FormData()
      form.append('file', fileData)
      form.append('shopsId', shopsId)
      form.append('merchantId', merchantId)
      await appSaveLogoInfoAPI(form)
  },
  setCurrentWorkData({commit}, payload) {
    commit('setCurrentWorkData', payload)
  }
}
export const mutations = {

  setCurrentWorkData(state, payload) {
    state.mobile.currentWorkData = payload;
  }
}

export const state = {
  mobile: {
    currentWorkData: null,
  }
}


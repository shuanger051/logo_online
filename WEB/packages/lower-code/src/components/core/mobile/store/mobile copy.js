import { takeScreenshot} from "@editor/utils/canvas-helper.js";
import {appSaveLogoInfoAPI, appSaveLogoInfoBase64API} from "core/api"
import router from '@/router'
import appStore from '@/store'
export const actions = {
  async mCreateCover({ commit, state, dispatch,rootState }, {el, file}) {
      let shopsId = router.currentRoute.query.shopId;
      let merchantId =  appStore.state.user.merchant.id
      let fileData = file
      let isbase64 = false;
      if (!fileData) {
        fileData = await takeScreenshot({selector: el, type: 'dataUrl'});
        isbase64 = true
      } 
      const form = new FormData()
      form.append('shopsId', shopsId)
      form.append('merchantId', merchantId)

      if (!isbase64) {
        form.append('file', fileData)
        await appSaveLogoInfoAPI(form)
      } else {
        form.append('base64', fileData)
        await appSaveLogoInfoBase64API(form)
      }
  
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


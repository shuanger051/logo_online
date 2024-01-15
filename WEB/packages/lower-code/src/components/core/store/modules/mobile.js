import { takeScreenshot} from "@editor/utils/canvas-helper.js";
import {appSaveLogoInfoAPIOSS,appSaveLogoInfoBase64APIOSS} from "core/api"
import router from '@/router'
import appStore from '@/store'
export const actions = {
  async mCreateCover({ commit, state, dispatch,rootState }, {el, file}) {
      // commit('setEditingElement', null)
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
      console.log(typeof appSaveLogoInfoBase64APIOSS, 111)
      if (!isbase64) {
        form.append('file', fileData)
        await appSaveLogoInfoAPIOSS(form)
      } else {
        form.append('base64', fileData)
        await appSaveLogoInfoBase64APIOSS(form)
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


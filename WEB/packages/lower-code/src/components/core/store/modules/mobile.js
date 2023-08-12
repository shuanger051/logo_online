import { Notify } from 'vant';
import { takeScreenshot,downloadPoster} from "@editor/utils/canvas-helper.js";
import {appSaveLogoInfoAPI} from "core/api"
import 'formdata-polyfill'
export const actions = {
  async mCreateCover({ commit, state, dispatch,rootState }, {el}) {
    commit('setEditingElement', null)
    let flag = true
    try {
      const file = await takeScreenshot({selector: el});
      const form = new FormData()
      form.append('file', file)
      form.append('shopsId', 10)
      form.append('merchantId', 6)
      const res = await appSaveLogoInfoAPI(form)
      commit('setShopSignr', res.data.urlPath)
      Notify({ type: 'success', message: '生成图片成功' })

    } catch (e) {
      console.log(e)

      Notify({ type: 'danger', message: '生成图片失败' })
      flag = false
    } finally {
    }
    return flag
  },

}
export const mutations = {

  setShopSignr(state, payload) {
    state.mobile.currentShopSign = payload;
  }
}

export const state = {
  mobile: {
    currentShopSign: ''
  }
}


import Element from "core/models/element";
import { message } from "ant-design-vue";
import Vue from "vue";
import Page from "core/models/page";
import Work from "core/models/work";
import {
  saveTemplate,
  updateTemplate,
  getTemplateByID,
  uploadMaterialAttachment,
} from "core/api";
import { takeScreenshot,downloadPoster } from "@editor/utils/canvas-helper.js";
const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};

const createSaveLoading = (dispatch) => (text) =>  {
  dispatch('loading/update', {
    type: 'saveWork_tip',
    payload: text
  },{
    root: true
  })
}
const showOrHideLoading = (dispatch) => (flag) => {
  dispatch('loading/update', {
    type: 'saveWork_loading',
    payload: flag
  }, {
    root: true
  }) 
}

export const actions = {
  previewWork({ commit }, payload = {}) {
    commit("previewWork", payload);
  },
  deployWork({ commit }, payload = {}) {
    commit("previewWork", payload);
  },
  updateWork({ commit, state }, payload = {}) {
    // update work with strapi
    const work = {
      ...state.work,
      ...payload,
    };
    commit("setWork", work);
  },

  async saveWork({ commit, dispatch, state }) {
    const { router, handlerSaveSucessJump } = Vue.prototype.$editorConfig;
    const {currentRoute} = router
    const flag = await dispatch('createCover')
    if (flag) return 
    const data = {
      name: state.work.title,
      style: state.work.style,
      isSimpleTpl: state.work.isSimpleTpl,
      material: state.work.material,
      domItem: JSON.stringify(state.work),
    };
    if (currentRoute.params.id) {
      data.id = currentRoute.params.id;
      await updateTemplate(data);
      message.success("保存成功");
    } else {
      await saveTemplate(data);
      message.success("保存成功");
      await sleep(2);
      handlerSaveSucessJump();
    }
  },
  fetchWork({ commit, dispatch, state, rootState }, obj) {
    let id
    let hasWork = false
    if (typeof obj == 'object') {
      id = obj.id
      hasWork = obj.hasWork
    } else {
      id = obj
    }
    if (hasWork) {
      commit("setWork", state.mobile.currentWorkData);
      commit("setEditingPage");
      return
    }
    return getTemplateByID({
      id,
    }).then((entry) => {
      const { data } = entry;
      try {
        const str = data.domItem;
        const workData = JSON.parse(str);
        commit("setWork", workData);
        commit("setEditingPage");
      } catch (e) {
        Vue.prototype.$message.error("请输入正确的id");
      }
    });
  },
  async createCover({ commit, state, dispatch,rootState }) {
    let flag = false
    const handlerLoading = showOrHideLoading(dispatch)
    const loadingTip = createSaveLoading(dispatch)
    commit('setEditingElement', null)
    try {
      handlerLoading(true)
      loadingTip("正在生成封面图片...")
      await sleep(500);
      const file = await takeScreenshot();
      loadingTip("正在上传封面图片...")
      const form = new FormData()
      form.append('file', file)
      const info = await uploadMaterialAttachment(form);
      if (info.code == "0") {
        state.work.cover_image_url = info.data.urlPath
      }
    } catch (e) {
      message.error('生成图片失败')
      flag = true
    } finally {
        handlerLoading(false)
    }
    return flag
  },
  downloadPoster({ commit, state, dispatch }) {
    downloadPoster();
  },
};

// mutations
export const mutations = {
  /**
   *
   * @param {*} state
   * @param {Object} payload
   *

   */
  setWorkCover(state, { type, value }) {
    const [cover] = value;
    state.work.cover_image_url = cover.url;
  },
  setWork(state, work) {
    window.__work = work;
    work.pages = work.pages.map((page) => {
      page.elements = page.elements.map((element) => {
        return new Element(element).completeAttr()

      });
      return new Page(page);
    });
    state.work = new Work(work);
  },
  previewWork(state, { type, value }) {},
  deployWork(state, { type, value }) {},
  formDetailOfWork(state, { type, value }) {
    state.formDetailOfWork = value;
  },
};

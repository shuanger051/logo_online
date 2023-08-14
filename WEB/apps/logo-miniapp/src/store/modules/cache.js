import { commonService } from "@/apis";
import Vue from "vue";
export default {
  namespaced: true,
  state: {
    // 字典项
    dictionary: {},
  },
  getters: {
    // dictionary(state) {
    //   return state.dictionary;
    // },
  },
  actions: {
    // 查询字典项
    queryDictByKey({ state, commit }, { keys = [] }) {
      // 判断类型
      if (typeof keys == "string") keys = [keys];
      // 遍历查询
      keys.forEach((key) => {
        const cKey = ["dict", key].join("__");
        // 字典项已存在
        if (state.dictionary[key]) return;
        // 不存在则请求
        commonService
          .getItemsByDictKeyInDB({ dictKey: key })
          // 缓存数据字典项
          .then((res) => {
            const list = _.get(res, "data", []);
            commit("setDictCache", { key: cKey, val: list });
          });
      });
    },
  },
  mutations: {
    // 设置字典项缓存
    setDictCache(state, { key, val }) {
      Vue.set(state.dictionary, key, val);
    },
    // 设置缓存
    setCache(state, { key, val }) {
      Vue.set(state, key, val);
    },
    delCache() {},
  },
};

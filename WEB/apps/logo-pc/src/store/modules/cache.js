import { commonService } from "@/services";
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
        const cVal = state.dictionary[key];
        // 字典项已存在
        if (_.isArray(cVal) && cVal.length) return;
        // 不存在则请求
        commonService
          .getItemsByDictKeyInDB({ dictKey: key })
          // 缓存数据字典项
          .then((res) => {
            const list = _.get(res, "data", []);
            commit("setDictCache", { key: cKey, val: list });
          })
          .catch((err) => {
            console.warn("字典项获取失败!");
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

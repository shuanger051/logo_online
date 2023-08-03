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

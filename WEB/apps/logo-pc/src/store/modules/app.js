const state = {
  // 是否适老版
  isOldVersion: false,
  // 是否已阅读负面清单
  isReadNegative: false,
};

const mutations = {
  // 设置适老版
  setIsOldVersion(state, val) {
    state.isOldVersion = val;
  },
  // 设置阅读状态
  setIsReadNegative(state, val) {
    state.isReadNegative = val;
  },
};

export default {
  namespaced: true,
  mutations,
  state,
};

const state = {
  // 是否适老版
  isOldVersion: false,
};

const mutations = {
  // 设置适老版
  setIsOldVersion(state, val) {
    state.isOldVersion = val;
  },
};

export default {
  namespaced: true,
  mutations,
  state,
};

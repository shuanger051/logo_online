const state = {
  // token
  token: null,
  // 用户信息
  profiles: {},
};

const mutations = {
  // 设置token
  setToken(state, val) {
    state.token = val;
  },
  // 设置用户信息
  setUserInfo(state, detail) {
    state.profiles = detail;
  },
};

export default {
  namespaced: true,
  mutations,
  state,
};
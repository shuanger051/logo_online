const state = {
  // token
  token: null,
  // 用户信息
  profiles: {},
  // 商户信息
  merchant: {},
};

const mutations = {
  // 设置token
  setToken(state, val) {
    state.token = val;
    sessionStorage.setItem("token", val);
  },
  // 设置用户信息
  setUserInfo(state, detail) {
    state.profiles = detail;
  },
  // 设置商户信息
  setMerchantInfo(state, detail) {
    state.merchant = detail;
  },
};
export default {
  namespaced: true,
  mutations,
  state,
};


export default {
  namespaced: true,
  state: {
   token: null
  },
  actions: {
    setToken({state, commit}, val) {
      commit('setToken', val)
    }
  },
  mutations: {
    setToken(state, val) {
      state.token = val;
      sessionStorage.setItem("token", val);
    },
  }
}

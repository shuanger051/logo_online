import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";
import app from './modules/app';
import user from "./modules/user";
import cache from "./modules/cache";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    app,
    user,
    cache,
  },
  plugins: [
    createPersistedState({
      storage: sessionStorage,
    }),
  ],
});

import Vue from 'vue'
import Vuex from 'vuex'
import modules from './modules'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)
const store = new Vuex.Store({
  modules,
  plugins: [
    createPersistedState({
      storage: sessionStorage,
      paths: ['user']
    }),
  ],
})

export default store

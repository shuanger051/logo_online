
import Vue from 'vue'
import Vuex from 'vuex'
import editor from './modules/editor'
import {actions, state, mutations} from './modules/mobile.js'

Vue.use(Vuex)

Object.assign(editor.actions, actions)
Object.assign(editor.state, state)
Object.assign(editor.mutations, mutations)

export default new Vuex.Store({
  state: {

  },
  mutations: {

  },
  actions: {

  },
  modules: {
    editor,
  },
  plugins: []
})

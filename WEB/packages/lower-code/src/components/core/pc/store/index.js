
import Vue from 'vue'
import Vuex from 'vuex'
import editor from 'core/store/modules/editor'
import {actions, state, mutations} from './pc.js'

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

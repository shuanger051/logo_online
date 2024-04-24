
import Vue from 'vue'
import Vuex from 'vuex'
import editor from 'core/store/modules/editor'
import createSignboardPersisted from 'core/store/plugins/createSignboardPersisted'
import {actions, state, mutations} from 'core/store/modules/pic-edit.js'

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
  plugins: [
    createSignboardPersisted({
      key: 'signboard-mobile'
    }),
  ]
})

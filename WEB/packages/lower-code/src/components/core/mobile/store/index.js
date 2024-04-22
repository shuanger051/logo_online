
import Vue from 'vue'
import Vuex from 'vuex'
import editor from 'core/store/modules/editor'
import {actions, state, mutations} from 'core/store/modules/pic-edit.js'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)

Object.assign(editor.actions, actions)
Object.assign(editor.state, state)
Object.assign(editor.mutations, mutations)

const mutationKey = ['setElementCommonStyle', 'elementManager']
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
    createPersistedState({
      storage: localStorage,
      key: 'signboard-mobile',
      paths: [
        'editor.work'
      ],
      filter(m) {
        return mutationKey.some((item) => {
          return (m.type || '').includes(item)
        })
      }
    }),
  ]
})

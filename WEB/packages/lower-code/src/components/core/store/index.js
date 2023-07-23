/*
 * @Author: ly525
 * @Date: 2020-10-11 10:13:51
 * @LastEditors: ly525
 * @LastEditTime: 2020-10-11 11:16:37
 * @FilePath: /luban-h5/front-end/h5/src/components/core/store/index.js
 * @Github: https://github.com/ly525/luban-h5
 * @Description: Do not edit
 * @Copyright 2018 - 2019 luban-h5. All Rights Reserved
 */
import Vue from 'vue'
import Vuex from 'vuex'
import undoRedoPlugin from './plugins/undo-redo/index'
import editor from './modules/editor'
import loading from './modules/loading'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {

  },
  mutations: {

  },
  actions: {

  },
  modules: {
    editor,
    loading,
  },
  plugins: [undoRedoPlugin]
})

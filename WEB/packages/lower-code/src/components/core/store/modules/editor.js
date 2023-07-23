// initial state
import Work from 'core/models/work'
import { actions as canvasActions, mutations as canvasMutations } from './canvas'
import { actions as pageActions, mutations as pageMutations } from './page'
import { actions as elementActions, mutations as elementMutations } from './element'
import { actions as workActions, mutations as workMutations } from './work'
import { actions as dataSourceActions, mutations as dataSourceMutations } from './data-source'

const state = {
  works: [],
  work: new Work(),
  editingPage: { elements: [] },
  editingElement: null,
  formDetailOfWork: {
    uuidMap2Name: {},
    formRecords: []
  },
  workTemplates: [],
  scaleRate: 1,
  scripts: [],
  test: {
    name:11
  }
}

// getters
const getters = {}

// actions
const actions = {
  ...elementActions,
  ...pageActions,
  ...workActions,
  ...canvasActions,
  ...dataSourceActions
}

// mutations
const mutations = {
  ...elementMutations,
  ...pageMutations,
  ...workMutations,
  ...canvasMutations,
  ...dataSourceMutations
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}

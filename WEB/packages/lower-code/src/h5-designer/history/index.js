import { difference } from 'lodash'
import undoRedo from './undoRedo'

let writeActionList = [
  'cms/elements', 'cms/events', 'cms/pages', 'cms/editState', 'cms/works'
]
let blackActionList = [
  'cms/pages/createPage'
]
export default (store) => {
  let undoRedoPlugin = undoRedo({
    // 过滤action
    // 需要过滤返回true，不过滤返回false
    ignoreAction: [
      // 白名单黑名单过滤
      (action) => {
        let wflag = writeActionList.some((item) => {
          return action.type.indexOf(item) > -1
        })
        if (!wflag) {
          return true
        }
        let bflag = blackActionList.some((item) => {
          return action.type.indexOf(item) > -1
        })
        return bflag
      },
      // 过滤更新元素style相关的action策略
      // 1. 当连续两个action都为updateElementStyle, 把最后一个替换
      // 2. 当updateElementStyle前面一个action为新建元素，只保留最后一个
      (action, undo) => {
        let name = ''
        let addEl = 'cms/elements/addElement'
        if (action.type == name) {
          let lastAction = undo.indexOf(undo.length - 1)
          let secondAction = undo.indexOf(undo.length - 2)
          if (
            (secondAction &&
            secondAction.type == name &&
            lastAction.type == name &&
            !difference(Object.keys(secondAction.payload), Object.keys(action.payload)).length &&
            !difference(Object.keys(lastAction.payload), Object.keys(action.payload)).length) ||
            (secondAction &&
            secondAction.type == addEl &&
            lastAction.type == name)
          ) {
            undo.pools.pop()
            undo.pools.push(action)
            return true
          }
        }
      }
    ],
    recordList: ['elements', 'events', 'pages', 'editState', 'works']
  })

  return undoRedoPlugin(store)
}

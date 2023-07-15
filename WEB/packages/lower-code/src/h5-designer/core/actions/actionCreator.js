import actionCtr from './specail'

const REGBUILDIN = /^buildIn$/

/**
 * 关联action和存储器
 * @param {*} action
 * @param {*} accumulator
 */
const relateActionAndAccumulator = (action, accumulator) => {
  // 添加action
  accumulator.add(() => action.destroy())
  // action销毁时，消除存储器中的引用
  action.dep.add(() => {
    // 当存储器锁定的时候，禁止执行
    if (!accumulator.islocked()) {
      accumulator.delete(action)
    }
  })
}

/**
 * 创建单个action
 */
const createAction = (data, accumulator, context) => {
  let { result } = data
  let { type, value } = result
  let action
  if (result && REGBUILDIN.test(type)) {
    if (actionCtr[value]) {
      action = new actionCtr[value](result, context)
    } else {
      console.error('[action factory]: no ' + value + ' constructor')
    }
  }

  if (action) {
    relateActionAndAccumulator(action, accumulator)
  }
  return action
}

export default createAction

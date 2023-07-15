const simpleType = /^(number|string|boolean|function)$/

/**
 * ability基类
 */
export default class BaseAbility {
  setOptions(value, force) {
    if (force) {
      this.__options = value
      return this
    }

    this.__options = this.__options || {}

    function iteration (target, source) {
      let keys = Object.keys(source)

      for (var i = 0; i < keys.length; i++) {
        let key = keys[i]
        let targetItem = target[key]
        let sourceItem = source[key]
        if (
          simpleType.test(typeof sourceItem) ||
          typeof targetItem !== typeof sourceItem
        ) {
          target[key] = sourceItem
          continue
        }
        // 当都为数组进行合并
        if (Array.isArray(sourceItem)) {
          targetItem = targetItem.concat(sourceItem)
        } else if (typeof sourceItem == 'object') {
          targetItem = iteration(targetItem, sourceItem)
        } else {
          targetItem = sourceItem
        }
        target[key] = targetItem
      }

      return target
    }

    this.__options = iteration(this.__options, value)
    return this
  }

  get options () {
    return this.__options
  }

  destroy() {
    throw new Error('It needs to be implemented in subclasses')
  }
}

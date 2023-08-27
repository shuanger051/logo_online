/**
 * == 数据字典转对象 ==
 * @param {*} key
 * @returns
 */
export function mapDictObject(key) {
  return (state) => {
    const list = _.get(state, ["cache", "dictionary", `dict__${key}`], []);
    return list.reduce((dtm, item) => {
      const { itemKey, itemValue } = item;
      dtm[itemKey] = itemValue;
      return dtm;
    }, {});
  };
}

/**
 * == 获取字典项缓存 ==
 * @param {*} key
 * @returns
 */
export function mapDictArray(key) {
  return (state) => _.get(state, ["cache", "dictionary", `dict__${key}`], []);
}

/**
 * == create select options ==
 * @param {*} key 
 * @returns 
 */
export function mapDictOptions(key) {
  return (state) =>
    _.get(state, ["cache", "dictionary", `dict__${key}`], []).map((item) => {
      item.value = item.itemKey;
      item.label = item.itemValue;
      return item;
    });
}

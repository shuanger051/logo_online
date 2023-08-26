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
 * == 获取字典项（select option使用） ==
 * @param {*} key
 * @returns
 */
export function mapDictOptions(key) {
  return (state) =>
    _.get(state, ["cache", "dictionary", `dict__${key}`], []).map((item) => {
      item.text = item.itemValue;
      item.value = item.itemKey;
      return item;
    });
}

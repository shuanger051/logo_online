
const sign = {
  runtime: ['inited', 'destroyed', 'beforeDestroy'],
  component: ['click', 'destroyed', 'beforeDestroy', 'created']
}

const createSystemSign = () => {
  let obj = {}

  for (var i in sign) {
    obj[i] = {}

    for (var j = 0; j < sign[i].length; j++) {
      let key = sign[i][j]
      obj[i][key] = i + '_' + key
    }
  }
  return obj
}

const systemSign = createSystemSign()

export default systemSign

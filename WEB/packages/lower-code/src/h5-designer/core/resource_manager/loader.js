/**
 * 资源加载模块
 *
 */

function requirePs(id) {
  return new Promise((resolve, reject) => {
    requirejs(id, (data) => {
      resolve(data)
    }, (e) => {
      reject(e)
    })
  })
}

/**
 * 加载器
 * @param {*} urls
 * @returns
 */
function loader(urls) {
  if (!Array.isArray(urls)) {
    urls = [urls]
  }
  let all = []
  let len = urls.length

  for (var i = 0; i < len; i++) {
    let url = urls[i]
    all.push(requirePs(url))
  }
  return Promise.all(all)
}

export default loader

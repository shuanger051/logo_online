
const path = require('path')
module.exports = function mixWebpack (conifg) {
  conifg.resolve = conifg.resolve || {}
  conifg.resolve.alias = conifg.resolve.alias || {}
  Object.assign(conifg.resolve.alias, {
    '@editor': path.join(__dirname, 'src'),
    'core': path.join(__dirname, 'src/components/core')
  })
}
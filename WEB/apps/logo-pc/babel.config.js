const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)

const plugins = ['@babel/plugin-proposal-optional-chaining', [
  'component',
  {
    'libraryName': 'element-ui',
    'styleLibraryName': 'theme-chalk'
  }
]]
if (IS_PROD) {
  plugins.push('transform-remove-console')
}

module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset',
    ['@vue/babel-preset-jsx',
    {
      'injectH': false
    }]
  ],
  plugins
}

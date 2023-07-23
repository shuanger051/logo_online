/*
 * @Author: ly525
 * @Date: 2019-12-14 22:43:55
 * @LastEditors: ly525
 * @LastEditTime: 2020-10-11 11:44:14
 * @FilePath: /luban-h5/front-end/h5/vue.config.js
 * @Github: https://github.com/ly525/luban-h5
 * @Description: Do not edit
 * @Copyright 2018 - 2020 luban-h5. All Rights Reserved
 */
const path = require('path')
const webpack = require('webpack')
const mixWebpack = require('./webpackMixIn')
const target = 'http://localhost:1337'
const mainAppOutputDir = path.join(__dirname, '../../back-end/h5-api/build-editor')

let page = {
  entry: 'src/main.js',
  template: 'public/index.html',
  filename: 'index.html',
  title: 'Index Page',
  outputDir: mainAppOutputDir
}

const configureWebpack = {

  plugins: [
    // https://github.com/moment/moment/issues/2416
    new webpack.ContextReplacementPlugin(/moment\/locale$/, /(zh-cn)$/)
  ],
}

mixWebpack(configureWebpack);

module.exports = {
  outputDir: page.outputDir,
  publicPath: page.publicPath,
  lintOnSave: process.env.NODE_ENV !== 'production',
  productionSourceMap: process.env.NODE_ENV !== 'production',
  pages: { index: page },
  devServer: {
    proxy: {
      '^/auth|upload|content-manager|users-permissions|works|scripts|datasources|admin|psd-files|workforms|third-libs|engine-assets/': {
        target,
        changeOrigin: true,
        ws: false
      }
    }
  },
  configureWebpack,
  css: {
    loaderOptions: {
      stylus: {
        'resolve url': true,
        'import': [
          // './src/theme'
        ]
      }
    },
    extract: false
  },
}

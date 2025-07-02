const { WebpackPlugin } = require('@electron-forge/plugin-webpack');

module.exports = {
  packagerConfig: {}, // 可选：打包选项（如图标）
  makers: [
    {
      name: '@electron-forge/maker-dmg',
      config: {
        background: 'src/main/favicon/favicon.png',
        format: 'ULFO'
      }
    }
  ],
  plugins: [
    // new WebpackPlugin({
    //   // 主进程 Webpack 配置
    //   mainConfig: 'buildClient/webpack.main.config.js',
    //   // 渲染进程 Webpack 配置
    //   renderer: {
    //     config: 'buildClient/webpack.render.config.js',
    //     entryPoints: [{
    //       html: './src/render/electron.ejs', // 渲染进程 HTML 入口
    //       js: './src/render/electron.js',  // 渲染进程 JS 入口
    //       name: 'main_window',
    //       preload: {
    //         js: './src/preload/index.js'   // Preload 脚本入口
    //       }
    //     }]
    //   }
    // })
  ]
};
const path = require("path");
const webpack = require("webpack");
const themePath = path.resolve(__dirname, "./src/styles/theme/var.less");
const webpackMixin = require("@shop-sign/editor/webpackMixIn");

module.exports = {
  publicPath: process.env.VUE_APP_PUBLIC_PATH,
  productionSourceMap: false,
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          // less 文件覆盖（文件路径为绝对路径）
          hack: `true; @import "${themePath}";`,
        },
      },
      postcss: {
        config: {
          path: "./",
        },
      },
    },
  },
  devServer: {
    port: 8088,
    proxy: {
      "/api": {
        //此处要与 /services/api.js 中的 API_PROXY_PREFIX 值保持一致
        target: process.env.VUE_APP_API_BASE_URL,
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
  lintOnSave: false,

  configureWebpack: (config) => {
    // global var
    config.plugins.push(
      new webpack.ProvidePlugin({
        _: "lodash",
      })
    );
    webpackMixin(config);
  },
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = "菜单式店招设计与承诺备案";
      return args;
    });
  },
  pwa: {
    name: "菜单式店招设计与承诺备案",
    // themeColor
  },
};

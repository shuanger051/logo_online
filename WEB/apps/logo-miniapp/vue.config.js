const path = require("path");
const themePath = path.resolve(__dirname, "./src/styles/theme/var.less");
module.exports = {
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
};

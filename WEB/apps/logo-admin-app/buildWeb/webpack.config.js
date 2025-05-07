const path = require("path");
const VueLoaderPlugin = require("vue-loader/lib/plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const Autoprefixer = require("autoprefixer");
const styleResourcesLoader = require("style-resources-loader");
const { context } = require("../config/index.js");
const webpack = require("webpack");
const webpackMixin = require("@shop-sign/editor/webpackMixIn");
const dotenv = require("dotenv-webpack");
// 是否是调试模式
const devMode = process.env.NODE_ENV === "development";
var webpackBaseConfig = {
  mode: process.env.NODE_ENV,
  devtool: devMode ? "eval-source-map" : false,
  entry: {
    main: ["./src/render/index.js"],
  },
  output: {
    path: path.resolve(process.cwd(), "dist"),
    publicPath: "/",
    filename: `.${context.page}/js/[name]${
      devMode ? "" : "-[contenthash:8]"
    }.js`,
    chunkFilename: `.${context.page}/js/[name]${
      devMode ? "" : "-[contenthash:8]"
    }.js`,
  },
  optimization: {
    runtimeChunk: false,
    minimize: !devMode,
    splitChunks: {
      chunks: "initial",
      cacheGroups: {
        vendor: {
          test: /node_modules\//,
          name: "vendor",
          priority: 10,
          enforce: true,
        },
      },
    },
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: "vue-loader",
      },
      {
        test: /\.js$/,
        exclude: [/node_modules/],
        use: [
          {
            loader: "babel-loader",
            options: {
              cacheDirectory: true,
              presets: ["@babel/preset-env", "@vue/babel-preset-jsx"],
            },
          },
          {
            loader: path.resolve("buildClient/libs/conditionCompileLoader.js"),
            options: {
              conditions: {
                // 不配置默认为false
                ELECTRON: false,
                WEB: true,
              },
            },
          },
        ],
      },
      {
        test: /\.css$/,
        use: (devMode ? ["css-hot-loader"] : []).concat([
          MiniCssExtractPlugin.loader,
          {
            loader: "css-loader",
            options: {
              sourceMap: true,
            },
          },
          {
            loader: "postcss-loader",
            options: {
              plugins: [Autoprefixer],
            },
          },
        ]),
      },
      {
        test: /\.(scss|sass)$/,
        use: (devMode ? ["css-hot-loader"] : []).concat([
          {
            loader: "css-loader",
            options: {
              sourceMap: true,
            },
          },
          {
            loader: "postcss-loader",
            options: {
              plugins: [Autoprefixer],
            },
          },
          {
            loader: "sass-loader",
          },
        ]),
      },
      {
        test: /\.less$/,
        use: (devMode ? ["css-hot-loader"] : []).concat([
          MiniCssExtractPlugin.loader,
          {
            loader: "css-loader",
            options: {
              sourceMap: true,
            },
          },
          {
            loader: "postcss-loader",
            options: {
              plugins: [Autoprefixer],
            },
          },
          {
            loader: "less-loader",
            options: {
              lessOptions: {
                javascriptEnabled: true,
              },
            },
          },
          {
            loader: "style-resources-loader",
            options: {
              patterns: [
                path.resolve(__dirname, "../src/render/theme/theme.less"),
              ],
            },
          },
        ]),
      },
      {
        test: /\.(gif|svg|png|jpe?g|ico|hdr)(\?\S*)?$/,
        use: [
          {
            loader: "url-loader",
            options: {
              esModule: false,
              limit: 2048,
              name: `.${context.page}/images/[name].[contenthash:8].[ext]`,
            },
          },
        ],
      },
      {
        test: /\.(eot|ttf|woff|woff2|otf)(\?\S*)?$/,
        use: [
          {
            loader: "url-loader",
            options: {
              esModule: false,
              limit: 2048,
              name: `.${context.page}/images/[name].[ext]`,
            },
          },
        ],
      },
      {
        test: /\.(html|tpl)$/,
        loader: "html-loader",
      },
      {
        test: /\.md$/,
        use: [
          {
            loader: "html-loader",
          },
          {
            loader: "markdown-loader",
            options: {
              // Pass options to marked
              // See https://marked.js.org/using_advanced#options
            },
          },
        ],
      },
    ],
  },
  resolve: {
    extensions: [".js", ".json", ".vue"],
    fallback: {
      url: require.resolve("url"),
    },
    alias: {
      "@@": path.resolve(__dirname, "../src"),
      "@": path.resolve(__dirname, "../src/render"),
      "@config": path.resolve(__dirname, "../config"),
      "@images": path.resolve(__dirname, "../src/render/libs/images"),
      ...webpackMixin(),
    },
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "./src/render/index.ejs",
      filename: "./index.html",
      title: "店招",
      inject: "body",
      favicon: false,
    }),
    new MiniCssExtractPlugin({
        filename: `.${context.page}/css/[name]${devMode ? '' : '-[contenthash:8]'}.css`,
        chunkFilename: `.${context.page}/css/[name]${devMode ? '' : '-[contenthash:8]'}.css`,
        ignoreOrder: true
    }),
    new webpack.ProvidePlugin({
      _: "lodash"
    }),
    new dotenv({
      path: path.resolve(__dirname, "../.env"),
    }),
    new VueLoaderPlugin(),
    // new webpack.DefinePlugin({
    //   "process.env": JSON.stringify(process.env),
    // }),
  ],
};
module.exports = webpackBaseConfig;

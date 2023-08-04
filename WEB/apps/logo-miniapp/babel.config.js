module.exports = {
  presets: ["@vue/cli-plugin-babel/preset"],
  plugins: [
    // vant按需加载
    [
      "import",
      {
        libraryName: "vant",
        libraryDirectory: "es",
        style: (name) => `${name}/style/less`,
      },
      "vant",
    ],
  ],
};

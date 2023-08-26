module.exports = {
  root: true,
  env: {
    node: true,
  },
  globals: {
    _: true,
  },
  extends: ["plugin:vue/essential"],
  parserOptions: {
    parser: "babel-eslint",
  },
  extends: ["plugin:vue/recommended"],
};

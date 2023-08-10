import axios from "axios";
import store from "../store";
import router from "../router";
import evnetBus from "../core/eventBus";

// 默认配置
axios.defaults.timeout = 5000;
axios.defaults.withCredentials = true;
axios.defaults.baseURL = "/api";

// 请求拦截
axios.interceptors.request.use(function onFulfilled(config) {
  // 添加token
  const { user } = store.state;
  config.headers["token"] = user.token;
  return config;
});

// 响应拦截
axios.interceptors.response.use(
  // 响应成功
  function onFulfilled(response) {
    const { data } = response;
    // 为0则成功
    if (data.code === "0") return data;
    // 未登录或登录失效
    else if (data.code == "401") {
      router.push({ path: "/" });
      store.commit("user/setToken");
      store.commit("user/setUserInfo");
      evnetBus.$emit("login");
    }
    // 其他为失败
    else return Promise.reject(data);
  }
);

/**
 * axios post
 * @param {*} url
 * @param {*} config
 * @returns
 */
export function axiosPost(url, config) {
  return (data) => axios.post(url, data, config);
}

/**
 * axios get
 * @param {*} url
 * @param {*} config
 * @returns
 */
export function axiosGet(url, config = {}) {
  return (params) => axios.get(url, Object.assign(config, { params }));
}

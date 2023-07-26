// import Cookie from 'js-cookie'
// import store from "@/store"

import store from "@/store";

// 401拦截
const resp401 = {
  /**
   * 响应数据之前做点什么
   * @param response 响应对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {*}
   */
  onFulfilled(response, options) {
    const { message } = options;
    if (response.code === 401) {
      message.error("无此权限");
    }
    return response;
  },
  /**
   * 响应出错时执行
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const { message } = options;
    const { response } = error;
    if (response.status === 401) {
      message.error("无此权限");
    }
    return Promise.reject(error);
  },
};

const resp403 = {
  onFulfilled(response, options) {
    const { message } = options;
    if (response.code === 403) {
      message.error("请求被拒绝");
    }
    return response;
  },
  onRejected(error, options) {
    const { message } = options;
    const { response } = error;
    if (response.status === 403) {
      message.error("请求被拒绝");
    }
    return Promise.reject(error);
  },
};

const resCommon = {
  onFulfilled(response, options) {
    const { router } = options;
    const data = _.get(response, "data", {});
    // 为0则成功
    if (data.code === "0") return data;
    // 登录失效
    else if (data.code == "10022") {
      store.commit("account/setUser", null);
      store.commit("account/setPermissions", null);
      router.push({ path: "/login" });
    }
    // 其他为失败
    else return Promise.reject(data);
  },
};

const reqCommon = {
  /**
   * 发送请求之前做些什么
   * @param config axios config
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {*}
   */
  // onFulfilled(config, options) {
  //   const { message } = options
  //   const { url } = config
  //   const user = _.get(store.state, 'account.user')
  //   if (url.indexOf('login') === -1 && !user) {
  //     message.warning('认证 token 已过期，请重新登录')
  //   }
  //   return config
  // },
  /**
   * 请求出错时做点什么
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const { message } = options;
    message.error(error.message);
    return Promise.reject(error);
  },
};

export default {
  request: [reqCommon], // 请求拦截
  response: [resp401, resp403, resCommon], // 响应拦截
};

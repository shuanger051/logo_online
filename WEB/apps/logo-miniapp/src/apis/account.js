import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 登录  ==
 */
export const getTokenAPI = axiosGet("logo/app/getTokenAPI");

/**
 * == 注册 ==
 */
export const registerCustomerAPI = axiosPost("logo/app/registerCustomerAPI");

import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 登录  ==
 */
export const getTokenAPI = axiosPost("logo/app/getTokenAPI");

/**
 * == 注册 ==
 */
export const registerCustomerAPI = axiosPost("logo/app/registerCustomerAPI");

/**
 * == 获取登录信息 ==
 */
export const queryCustomerByIdAPI = axiosGet("logo/app/queryCustomerByIdAPI");

/**
 * == 浙里办登录 ==
 */
export const getZLBTokenAPI = axiosGet("logo/app/getZLBTokenAPI");
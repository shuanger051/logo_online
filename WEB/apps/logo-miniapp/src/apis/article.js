import { axiosGet } from "../utils/request";

/**
 * == 获取文章详情 ==
 */
export const getContentByIDAPI = axiosGet("logo/app/getContentByIDAPI");

/**
 * == 获取文章列表 ==
 */
export const getContentByChannelIdAPI = axiosGet("logo/app/getContentByChannelIdAPI")
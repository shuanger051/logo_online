import { axiosGet, axiosPost } from "../utils/request";

/**
 * == 获取公钥 ==
 */
export const getPublicKey = axiosGet("logo/crypto/getPublicKey");

/**
 * == 公钥加密 ==
 */
export const encrypt = axiosPost("logo/crypto/encrypt");

/**
 * == 获取字典项 ==
 */
export const getItemsByDictKeyInDB = axiosGet("logo/app/getItemsByDictKeyInDB");

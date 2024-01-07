export * as apiService from './api';
import * as article from './article'
import * as common from "./common";
import * as account from "./account";
import * as shop from "./shop";
import * as signboard from "./signboard";

/** 文章 */
export const articleService = article;
/** 用户 */
export const accountService = account;
/** 通用 */
export const commonService = common;
/** 商铺信息 */
export const shopService = shop;
/** 店招信息 */
export const signboardService = signboard;


import { axiosGet } from "../utils/request";

/** 分页查询日志列表 */
export const getLogInfoListByPage = axiosGet("/logo/log/getLogInfoListByPage");
/** 根据ID查询日志信息 */
export const getLogInfoById = axiosGet("/logo/log/getLogInfoById");
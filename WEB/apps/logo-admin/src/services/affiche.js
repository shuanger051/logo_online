import { axiosPost, axiosGet } from "../utils/request";


/** 分页查询栏目列表 */
export const getChannelListByPage = axiosGet("/logo/channel/getChannelListByPage");
/** 根据栏目ID查询栏目信息 */
export const getChannelByID = axiosGet("/logo/channel/getChannelByID");
/** 新增栏目信息 */
export const saveChannel = axiosPost("/logo/channel/saveChannel");
/** 修改栏目信息 */
export const updateChannel = axiosPost("/logo/channel/updateChannel");
/** 删除栏目信息 */
export const deleteChannelByID = axiosPost("/logo/channel/deleteChannelByID");
/** 查询可供下拉选择的栏目信息列表 */
export const getChannelList = axiosGet("/logo/channel/getChannelList");


/** 分页获取文章列表信息 */
export const getContentListByPage = axiosGet("/logo/content/getContentListByPage");
/** 根据文章ID查询文章详情 */
export const getContentById = axiosGet("/logo/content/getContentById");
/** 保存文章信息 */
export const saveContent = axiosPost("/logo/content/saveContent");
/** 上传文章附件 */
export const uploadContentAttachment = axiosPost("/logo/attachment/uploadContentAttachment");
/** 下载文章附件 */
export const downloadContentAttachment = axiosGet("/logo/attachment/downloadContentAttachment");
/** 审核文章信息 */
export const auditContent = axiosPost("/logo/content/auditContent");
/** 根据ID删除文章信息 */
export const deleteContentById = axiosPost("/logo/content/deleteContentById");
/** 根据附件名称删除附件信息 */
export const deleteAttachmentByName = axiosPost("/logo/content/deleteAttachmentByName");
/** 根据ID更新文章信息 */
export const updateContentById = axiosPost("/logo/content/updateContentById");
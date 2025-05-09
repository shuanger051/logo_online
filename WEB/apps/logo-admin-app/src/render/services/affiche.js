import { axiosPost, axiosGet } from "../utils/request";
import { loadScript, uploadJsFile } from "./util";
/** 分页查询栏目列表 */
export const getChannelListByPage = axiosGet(
  "/logo/channel/getChannelListByPage"
);
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
export const getContentListByPage = async ({
  pageNum,
  pageSize=30,
  channelId,
  title,
}) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/news.js"
  );
  const news = window.news;
  let list =
    news.data.filter((item) => {
      return (
        (!channelId || channelId == item.channelId) &&
        (!title || item.title.indexOf(title) > -1)
      );
    }) || [];
  list = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
  return {
    data: {
      total: list.length,
      list,
    },
  };
};
/** 根据文章ID查询文章详情 */
export const getContentById = axiosGet("/logo/content/getContentById");
/** 保存文章信息 */
export const saveContent = async (data) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/news.js"
  );
  const news = window.news;
  let id = 0
  news.data.forEach(element => {
    if (element.id > id) {
      id = element.id
    } 
  });
  id++
  data.id = id
  news.data.push(data);
  return uploadJsFile(`window.news = ${JSON.stringify(news)}`, "news")
};

/** 上传文章附件 */
export const uploadContentAttachment = axiosPost(
  "/logo/attachment/uploadContentAttachmentOSS",
  {
    headers: {
      "content-Type": "multipart/form-data",
    },
  }
);
/** 下载文章附件 */
export const downloadContentAttachment = axiosGet(
  "/logo/attachment/downloadContentAttachment"
);
/** 审核文章信息 */
export const auditContent = axiosPost("/logo/content/auditContent");

/** 根据ID删除文章信息 */
export const deleteContentById = async ({id}) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/news.js"
  );
  const news = window.news;
  const index = news.data.findIndex((item) => item.id == id);
  if (index > -1) {
    news.data.splice(index,1)
    return uploadJsFile(`window.news = ${JSON.stringify(news)}`, "news")
  }
};

/** 根据附件名称删除附件信息 */
export const deleteAttachmentByName = axiosPost(
  "/logo/content/deleteAttachmentByName"
);
/** 根据ID更新文章信息 */
export const updateContentById = async (data) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/news.js"
  );
  const news = window.news;
  const { id } = data;
  const index = news.data.findIndex((item) => item.id == id);
  if (index > -1) {
    news.data[index] = data;
  }
  return uploadJsFile(`window.news = ${JSON.stringify(news)}`, "news")
};

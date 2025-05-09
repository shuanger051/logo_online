import { axiosGet } from "../utils/request";

/**
 * == 获取文章详情 ==
 */
// export const getContentByIDAPI = axiosGet("logo/app/getContentByIDAPI");

/**
 * == 获取文章详情 ==
 */
// export const getContentByIDAPI = axiosGet("logo/app/getContentByIDAPI");
export const getContentByIDAPI = ({id}) => {
  let news = window.news;

    let content = news.data.find((item) => item.id === +id) || {};
  return Promise.resolve({
    data: {
      ...content
    },
  });
}

/**
 * == 获取文章列表 ==
 */
// export const getContentByChannelIdAPI = axiosGet("logo/app/getContentByChannelIdAPI")
export const getContentByChannelIdAPI = (params) => {
  let news = window.news;
  let channelId = params.channelId;
  let chanelList = news.data.filter((item) => item.channelId === +channelId) || []

  return Promise.resolve({
    data: {
      list: chanelList,
      total: chanelList.length,
    },
  });
};
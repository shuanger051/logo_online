import { axiosPost } from "@/utils/request";

/**
 * == 店招模版列表 ==
 */
export const queryTemplateListPageAPI = axiosPost(
  "/logo/app/queryTemplateListPageAPI"
);

import { axiosGet } from "@/utils/request";

/**
 * == 店招模版列表 ==
 */
export const queryTemplateListPageAPI = axiosGet(
  "/logo/app/queryTemplateListPageAPI"
);

export const querySimpleTemplateByRandAPI = axiosGet(
  "/logo/app/querySimpleTemplateByRandAPI"
);

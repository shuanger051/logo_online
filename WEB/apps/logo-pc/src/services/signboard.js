import { axiosGet } from "@/utils/request";

/**
 * == 店招模版列表 ==
 */
// export const queryTemplateListPageAPI = axiosGet(
//   "/logo/app/queryTemplateListPageAPI"
// );
export const queryTemplateListPageAPI = () => {
  return Promise.resolve(window.template)
}

export const querySimpleTemplateByRandAPI = axiosGet(
  "/logo/app/querySimpleTemplateByRandAPI"
);

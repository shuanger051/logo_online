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

// export const querySimpleTemplateByRandAPI = axiosGet(
//   "/logo/app/querySimpleTemplateByRandAPI"
// );

export const querySimpleTemplateByRandAPI = () => {
  const lists = window.template.data.list.filter((item) => {
    return item.isSimpleTpl == '1'
  })
  return Promise.resolve({
    data: {
      list: lists,
      total: lists.length,
    }
  })
}
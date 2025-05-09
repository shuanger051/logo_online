import { axiosPost, axiosGet } from "../utils/request";
import { loadScript, uploadJsFile } from "./util";

/** 分页查询店招信息 */
export const getLogoListByPage = axiosGet("/logo/logo/getLogoListByPageOSS");
/** 根据ID查询店招数据 */
export const getLogoInfoByID = axiosGet("/logo/logo/getLogoInfoByID");
/** 新增店招信息 */
export const saveLogoInfo = axiosPost("/logo/logo/saveLogoInfo");
/** 根据ID修改店招信息 */
export const updateLogoInfoById = axiosPost("/logo/logo/updateLogoInfoById");
/** 根据ID删除店招信息 */
export const deleteLogoInfoByID = axiosPost("/logo/logo/deleteLogoInfoByID");

/** 分页查询模板信息 */
export const getTemplateListByPage = async ({
  name,
  style,
  pageNum,
  pageSize = 30,
}) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/template.js"
  );
  const templates = window.template;
  const list =
    templates.data.list.filter((item) => {
      return (
        (!style || item.style.indexOf(style) > -1) &&
        (!name || item.name.indexOf(name) > -1)
      );
    }) || [];
  const currentPageList = list.slice(
    (pageNum - 1) * pageSize,
    pageNum * pageSize
  );
  return {
    data: {
      total: list.length,
      list: currentPageList,
    },
  };
};

/** 根据ID查询模板数据 */
export const getTemplateByID = async ({ id }) => {
  if (!window.template) {
    await loadScript(
      "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/template.js"
    );
  }
  const lists = window.template;
  const template = lists.data.list.find((item) => {
    return item.id == id;
  });
  return {
    data: template,
  };
};

export const updateTemplateById = async (data) => {
  if (!window.template) {
    await loadScript(
      "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/template.js"
    );
  }
  const lists = window.template;

  if (data.id) {
    const index = lists.data.list.findIndex((item) => {
      return item.id == data.id;
    })
    lists.data.list[index] = data
  } else{
    let maxId = 0
    lists.data.list.forEach((item) => {
      if (item.id > maxId) {
        maxId = item.id
      }
    })
    data.id = maxId + 1
    lists.data.list.push(data)
  }
  lists.data.total = lists.data.list.length
  return uploadJsFile(`window.template = ${JSON.stringify(lists)}`, "template")
}

/** 新增模板信息 */
export const saveTemplate = axiosPost("/logo/template/saveTemplate");
/** 修改模板信息 */
export const updateTemplate = axiosPost("/logo/template/updateTemplate");
/** 根据ID删除模板信息 */
export const deleteTemplateByID = async ({id}) => {
  await loadScript(
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/template.js"
  );
  const template = window.template;
  const index = template.data.list.findIndex((item) => item.id == id);
  if (index > -1) {
    template.data.list.splice(index,1)
    template.data.total = template.data.list.length
    return uploadJsFile(`window.template = ${JSON.stringify(template)}`, "template")
  }
};
/** 根据ID更改模板发布状态 */
export const updateTemplateStatusById = axiosPost(
  "/logo/template/updateTemplateStatusById"
);

/** 分页查询素材信息 */
export const getMaterialListByPage = axiosGet(
  "/logo/material/getMaterialListByPage"
);
/** 上传素材附件 */
export const uploadMaterialAttachment = axiosPost(
  "/logo/attachment/uploadMaterialAttachmentOSS"
);
/** 根据ID查询素材数据 */
export const getMaterialByID = axiosGet("/logo/material/getMaterialByID");
/** 新增素材信息 */
export const saveMaterial = axiosPost("/logo/material/saveMaterial");
/** 根据ID修改素材信息 */
export const updateMaterialById = axiosPost(
  "/logo/material/updateMaterialById"
);
/** 根据ID删除素材信息 */
export const deleteMaterialByID = axiosPost(
  "/logo/material/deleteMaterialByID"
);

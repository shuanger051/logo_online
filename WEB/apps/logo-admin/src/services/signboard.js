import { axiosPost,axiosGet } from "../utils/request";

/** 分页查询店招信息 */
export const getLogoListByPage = axiosGet("/logo/logo/getLogoListByPage");
/** 根据ID查询店招数据 */
export const getLogoInfoByID = axiosGet("/logo/logo/getLogoInfoByID");
/** 新增店招信息 */
export const saveLogoInfo = axiosPost("/logo/logo/saveLogoInfo");
/** 根据ID修改店招信息 */
export const updateLogoInfoById = axiosPost("/logo/logo/updateLogoInfoById");
/** 根据ID删除店招信息 */
export const deleteLogoInfoByID = axiosPost("/logo/logo/deleteLogoInfoByID");


/** 分页查询模板信息 */
export const getTemplateListByPage = axiosGet("/logo/template/getTemplateListByPage");
/** 根据ID查询模板数据 */
export const getTemplateByID = axiosGet("/logo/template/getTemplateByID");
/** 新增模板信息 */
export const saveTemplate = axiosPost("/logo/template/saveTemplate");
/** 修改模板信息 */
export const updateTemplate = axiosPost("/logo/template/updateTemplate");
/** 根据ID删除模板信息 */
export const deleteTemplateByID = axiosPost("/logo/template/deleteTemplateByID");
/** 根据ID更改模板发布状态 */
export const updateTemplateStatusById = axiosPost("/logo/template/updateTemplateStatusById");


/** 分页查询素材信息 */
export const getMaterialListByPage = axiosGet("/logo/material/getMaterialListByPage");
/** 上传素材附件 */
export const uploadMaterialAttachment = axiosPost("/logo/attachment/uploadMaterialAttachment");
/** 根据ID查询素材数据 */
export const getMaterialByID = axiosGet("/logo/material/getMaterialByID");
/** 新增素材信息 */
export const saveMaterial = axiosPost("/logo/material/saveMaterial");
/** 根据ID修改素材信息 */
export const updateMaterialById = axiosPost("/logo/material/updateMaterialById");
/** 根据ID删除素材信息 */
export const deleteMaterialByID = axiosPost("/logo/material/deleteMaterialByID");

import store from "@/store";
import { axiosGet, axiosPost } from "../utils/request";

/** 查询系统用户信息 */
export const getSysUserList = axiosGet("/logo/sys/user/getSysUserList");
/** 超管解除账号锁定状态 */
export const secureAccountLock = axiosPost("/logo/sys/user/secureAccountLock");
/** 重置系统用户密码 */
export const resetPwd = axiosPost("/logo/sys/user/resetPwd");
/** 新增系统用户信息 */
export const saveSysUser = axiosPost("/logo/sys/user/saveSysUser");
/** 更新系统用户信息 */
export const updateSysUser = axiosPost("/logo/sys/user/updateSysUser");
/** 系统用户修改密码 */
export const changePwd = axiosPost("/logo/sys/user/changePwd");
/** 导出系统用户列表 */
export const exportExcelByUserListPage = axiosGet(
  "/logo/sys/user/exportExcelByUserListPage"
);
/** 锁定账户 */
export const lockSysUser = axiosPost("/logo/sys/user/lockSysUser");
/** 导入系统用户表格 */
export const importExcelByUserList = axiosPost(
  "/logo/sys/user/importExcelByUserList"
);

/** 分页查询角色信息 */
export const getSysRoleListByPage = axiosGet(
  "/logo/sys/role/getSysRoleListByPage"
);
/** 新增角色信息 */
export const saveSysRole = axiosPost("/logo/sys/role/saveSysRole");
/** 根据ID修改角色信息 */
export const updateSysRoleById = axiosPost("/logo/sys/role/updateSysRoleById");
/** 根据ID删除角色信息 */
export const deleteSysRoleById = axiosPost("/logo/sys/role/deleteSysRoleById");
/** 根据ID修改角色状态 */
export const updateSysRoleStatusById = axiosPost(
  "/logo/sys/role/updateSysRoleStatusById"
);
/** 根据ID查询角色信息 */
export const getSysRoleById = axiosGet("/logo/sys/role/getSysRoleById");

/** 根据ID查询权限信息 */
export const getSysPermissionById = axiosGet(
  "/logo/sys/permission/getSysPermissionById"
);
/** 分页查询权限信息 */
export const getSysPermissionListByPage = axiosGet(
  "/logo/sys/permission/getSysPermissionListByPage"
);
/** 查询所有权限信息 */
export const getSysPermissionList = axiosGet(
  "/logo/sys/permission/getSysPermissionList"
);
/** 根据角色ID查询权限信息 */
export const getPermissionByRoleId = axiosGet(
  "/logo/sys/permission/getPermissionByRoleId"
);
/** 新增权限信息 */
export const saveSysPermission = axiosPost(
  "/logo/sys/permission/saveSysPermission"
);
/** 根据ID修改权限信息 */
export const updateSysPermissionById = axiosPost(
  "/logo/sys/permission/updateSysPermissionById"
);
/** 根据ID删除权限信息 */
export const deleteSysPermissionById = axiosPost(
  "/logo/sys/permission/deleteSysPermissionById"
);

/** 数据字典分页查询 */
export const getDictListByPage = axiosGet("/logo/sys/dict/getDictListByPage");
/** 数据字典类型数据添加 */
export const saveDict = axiosPost("/logo/sys/dict/saveDict");
/** 数据字典类型数据修改 */
export const updateDictById = axiosPost("/logo/sys/dict/updateDictById");
/** 根据数据字典ID查询字典 */
export const getDictById = axiosGet("/logo/sys/dict/getDictById");
/** 根据数据字典ID删除字典 */
export const deleteDictById = axiosPost("/logo/sys/dict/deleteDictById");
/** 数据字典子项分页查询 */
export const getDictItemListByPage = axiosGet(
  "/logo/sys/dict-item/getDictItemListByPage"
);
/** 根据数据字典KEY查询子项列表 */
export const getItemsByDictKey = axiosGet(
  "/logo/sys/dict-item/getItemsByDictKey"
);
/** 根据数据字典子项ID查询子项列表 */
export const getDictItemById = axiosGet("/logo/sys/dict-item/getDictItemById");
/** 数据字典子项添加 */
export const saveDictItem = axiosPost("/logo/sys/dict-item/saveDictItem");
/** 根据数据字典子项ID修改字典子项 */
export const updateDictItemById = axiosPost(
  "/logo/sys/dict-item/updateDictItemById"
);
/** 根据ID删除数据字典子项 */
export const deleteDictItemById = axiosPost(
  "/logo/sys/dict-item/deleteDictItemById"
);
/** 查询字典子项 */
export const getItemsByDictKeyInDB = axiosGet(
  "/logo/sys/dict-item/getItemsByDictKeyInDB"
);

/** 根据ID查询角色权限信息 */
export const getSysRolePermissionById = axiosGet(
  "/logo/sys/role-permission/getSysRolePermissionById"
);
/** 根据角色ID查询角色权限信息 */
export const getSysRolePermissionListByRoleId = axiosGet(
  "/logo/sys/role-permission/getSysRolePermissionListByRoleId"
);
/** 批量新增或更新角色权限信息 */
export const saveOrUpdateSysRolePermissionForBatch = axiosPost(
  "/logo/sys/role-permission/saveOrUpdateSysRolePermissionForBatch"
);
/** 分页查询角色权限信息 */
export const getSysRolePermissionListByPage = axiosGet(
  "/logo/sys/role-permission/getSysRolePermissionListByPage"
);
/** 新增角色权限信息 */
export const saveSysRolePermission = axiosPost(
  "/logo/sys/role-permission/saveSysRolePermission"
);
/** 根据ID修改角色权限信息 */
export const updateSysRolePermissionById = axiosPost(
  "/logo/sys/role-permission/updateSysRolePermissionById"
);
/** 根据ID删除角色权限信息 */
export const deleteSysRolePermissionById = axiosPost(
  "/logo/sys/role-permission/deleteSysRolePermissionById"
);

/** 根据ID查询用户角色信息 */
export const getSysUserRoleById = axiosGet(
  "/logo/sys/user-role/getSysUserRoleById"
);
/** 分页查询用户角色信息 */
export const getSysUserRoleListByPage = axiosGet(
  "/logo/sys/user-role/getSysUserRoleListByPage"
);
/** 新增用户角色信息 */
export const saveSysUserRole = axiosPost("/logo/sys/user-role/saveSysUserRole");
/** 根据ID修改用户角色信息 */
export const updateSysUserRoleById = axiosPost(
  "/logo/sys/user-role/updateSysUserRoleById"
);
/** 根据ID删除用户角色信息 */
export const deleteSysUserRoleById = axiosPost(
  "/logo/sys/user-role/deleteSysUserRoleById"
);
/** 批量新增或修改用户角色信息 */
export const saveOrUpdateSysUserRoleForBatch = axiosPost(
  "/logo/sys/user-role/saveOrUpdateSysUserRoleForBatch"
);

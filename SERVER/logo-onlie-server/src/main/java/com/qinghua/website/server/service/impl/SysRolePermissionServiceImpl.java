package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysPermissionMapper;
import com.qinghua.website.server.dao.SysRoleMapper;
import com.qinghua.website.server.dao.SysRolePermissionMapper;
import com.qinghua.website.server.domain.SysPermissionDTO;
import com.qinghua.website.server.domain.SysRoleDTO;
import com.qinghua.website.server.domain.SysRolePermissionDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysRolePermissionServiceImpl
 * @Description 系统权限管理接口实现
 **/
@Service
public class SysRolePermissionServiceImpl implements SysRolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据用户ID查询角色权限信息
     *
     * @param id
     * @return
     */
    @Override
    public SysRolePermissionDTO getSysRolePermissionById(Long id) {
        return sysRolePermissionMapper.getSysRolePermissionById(id);
    }

    /**
     * 查询角色权限信息集合
     *
     * @param sysRolePermissionDTO
     * @return
     */
    @Override
    public List<SysRolePermissionDTO> getSysRolePermissionList(SysRolePermissionDTO sysRolePermissionDTO) {
        return sysRolePermissionMapper.getSysRolePermissionList(sysRolePermissionDTO);
    }

    /**
     * 分页查询角色权限信息集合
     *
     * @param sysRolePermissionDTO
     * @return
     */
    @Override
    public PageInfo<SysRolePermissionDTO> getSysRolePermissionListByPage(SysRolePermissionDTO sysRolePermissionDTO) {
        PageHelper.startPage(sysRolePermissionDTO.getPageNum(), sysRolePermissionDTO.getPageSize());
        List<SysRolePermissionDTO> sysRolePermissionDTOList = sysRolePermissionMapper.getSysRolePermissionList(sysRolePermissionDTO);
        return new PageInfo<>(sysRolePermissionDTOList);
    }

    /**
     * 保存角色权限信息
     *
     * @param bean
     */
    @Override
    public void saveSysRolePermission(SysRolePermissionDTO bean) {

        //判断roleID是否有效
        SysRoleDTO roleDTO = new SysRoleDTO();
        roleDTO.setId(bean.getRoleId());
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(roleDTO);
        Preconditions.checkNotNull(resRole, "不存在ID为：" + bean.getRoleId() + "的角色");

        //判断permissionID是否有效
        SysPermissionDTO resPerms = sysPermissionMapper.getSysPermissionById(bean.getPermissionId());
        Preconditions.checkNotNull(resPerms, "不存在ID为：" + bean.getPermissionId() + "的权限");

        //判断数据是否重复
        SysRolePermissionDTO rolePermissionDTO = sysRolePermissionMapper.getSysRolePermsByRoleIdANDPermsId(bean);
        if (null != rolePermissionDTO) {
            throw new BizException(SysConstant.ERROR_USER_REPEAT_ROLEPERMS);
        }

        sysRolePermissionMapper.saveSysRolePermission(bean);
    }

    /**
     * 更新角色权限信息
     *
     * @param bean
     */
    @Override
    public void updateSysRolePermissionById(SysRolePermissionDTO bean) {

        SysRolePermissionDTO resRPDTO = sysRolePermissionMapper.getSysRolePermissionById(bean.getId());
        Preconditions.checkNotNull(resRPDTO, "不存在ID为：" + bean.getId() + "的角色权限信息");

        //判断roleID是否有效
        SysRoleDTO roleDTO = new SysRoleDTO();
        roleDTO.setId(bean.getRoleId());
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(roleDTO);
        Preconditions.checkNotNull(resRole, "不存在ID为：" + bean.getRoleId() + "的角色");

        //判断permissionID是否有效
        SysPermissionDTO resPerms = sysPermissionMapper.getSysPermissionById(bean.getPermissionId());
        Preconditions.checkNotNull(resPerms, "不存在ID为：" + bean.getPermissionId() + "的权限");

        //判断数据是否重复
        SysRolePermissionDTO rolePermissionDTO = sysRolePermissionMapper.getSysRolePermsByRoleIdANDPermsId(bean);
        if (null != rolePermissionDTO && !rolePermissionDTO.getId().equals(bean.getId())) {
            throw new BizException(SysConstant.ERROR_USER_REPEAT_ROLEPERMS);
        }

        sysRolePermissionMapper.updateSysRolePermissionById(bean);
    }

    /**
     * 根据id删除角色权限信息
     *
     * @param id
     */
    @Override
    public void deleteSysRolePermissionById(Long id) {
        SysRolePermissionDTO resRPDTO = sysRolePermissionMapper.getSysRolePermissionById(id);
        Preconditions.checkNotNull(resRPDTO, "不存在ID为：" + id + "的角色权限信息");

        SysRoleDTO roleDTO = new SysRoleDTO();
        roleDTO.setId(resRPDTO.getRoleId());
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(roleDTO);
        Preconditions.checkNotNull(resRole, "不存在RoleID为：" + resRPDTO.getRoleId() + "的角色");

        sysRolePermissionMapper.deleteSysRolePermissionById(id);

    }

    /**
     * 批量新增角色权限信息
     *
     * @param sysRolePermissionDTOList
     */
    @Override
    public void saveSysRolePermissionForBatch(List<SysRolePermissionDTO> sysRolePermissionDTOList) {

        //判断传入的数据是否包含非同一个渠道下的数据，如果有则应提示数据非法
        for (int i = 0;i < sysRolePermissionDTOList.size();i++){
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            sysRoleDTO.setId(sysRolePermissionDTOList.get(i).getRoleId());
            SysRoleDTO res = sysRoleMapper.getSysRoleById(sysRoleDTO);
            if(null == res){
                throw new BizException(SysConstant.ERROR_USER_NOT_HAVE_ROLE_2);
            }
        }

        List<SysRolePermissionDTO> delParamList = new ArrayList<>();
        //清除传入参数中重复的userId值
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < sysRolePermissionDTOList.size(); i++) {
            SysRolePermissionDTO sysRolePermissionDTO = sysRolePermissionDTOList.get(i);
            if (!map.containsKey(sysRolePermissionDTO.getRoleId())) {
                delParamList.add(sysRolePermissionDTO);
                map.put(sysRolePermissionDTO.getRoleId(), i);
            }
        }

        //数据传入后，首先清空当前用户所有角色，然后再新增用户角色数据
        sysRolePermissionMapper.deleteSysRolePermissionByUserIdForBatch(delParamList);

        //批量新增角色权限数据信息
        sysRolePermissionMapper.saveSysRolePermissionForBatch(sysRolePermissionDTOList);
    }

    /**
     * 根据角色ID查询角色权限
     */
    @Override
    public List<SysRolePermissionDTO> getSysRolePermissionListByRoleId(Long roleId) {

        //角色信息是按照渠道划分，除超级管理员外用户只能查询自己所属渠道下的信息,判断当前用户所属渠道下面是否有当前角色ID
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        sysRoleDTO.setId(roleId);
        SysRoleDTO roleDTO = sysRoleMapper.getSysRoleById(sysRoleDTO);
        if(null != roleDTO){
            return  sysRolePermissionMapper.getSysRolePermissionListByRoleId(roleId);
        }

        throw new BizException(SysConstant.ERROR_USER_NOT_HAVE_ROLE);

    }

}

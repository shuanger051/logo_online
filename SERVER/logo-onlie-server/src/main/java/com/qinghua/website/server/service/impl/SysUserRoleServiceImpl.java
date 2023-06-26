package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysRoleMapper;
import com.qinghua.website.server.dao.SysUserMapper;
import com.qinghua.website.server.dao.SysUserRoleMapper;
import com.qinghua.website.server.domain.SysRoleDTO;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.domain.SysUserRoleDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysRoleServiceImpl
 * @Description 系统权限管理接口实现
 * @Author baichuan
 * @Date 2020/10/10 15:29
 **/
@Slf4j
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户ID查询用户角色信息
     * @param id
     * @return
     */
    @Override
    public SysUserRoleDTO getSysUserRoleById(Long id) {
        return sysUserRoleMapper.getSysUserRoleById(id);
    }

    /**
     * 查询用户角色信息集合
     * @param sysUserRoleDTO
     * @return
     */
    @Override
    public List<SysUserRoleDTO> getSysUserRoleList(SysUserRoleDTO sysUserRoleDTO) {
        return sysUserRoleMapper.getSysUserRoleList(sysUserRoleDTO);
    }

    /**
     * 分页查询用户角色信息
     * @param sysUserRoleDTO
     * @return
     */
    @Override
    public PageInfo<SysUserRoleDTO> getSysUserRoleListByPage(SysUserRoleDTO sysUserRoleDTO){
        PageHelper.startPage(sysUserRoleDTO.getPageNum(),sysUserRoleDTO.getPageSize());
        List<SysUserRoleDTO> sysUserRoleDTOList = sysUserRoleMapper.getSysUserRoleList(sysUserRoleDTO);
        return new PageInfo<>(sysUserRoleDTOList);
    }

    /**
     * 新增用户角色信息
     * @param sysUserRoleDTO
     */
    @Override
    public void saveSysUserRole(SysUserRoleDTO sysUserRoleDTO){
        //判断数据是否已存在
        SysUserRoleDTO userRoleDTO = sysUserRoleMapper.getSysUserRoleByUserIdANDRoleId(sysUserRoleDTO);
        if(null != userRoleDTO){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_USERROLE);
        }
        sysUserRoleMapper.saveSysUserRole(sysUserRoleDTO);
    }

    /**
     * 更新用户角色信息
     * @param sysUserRoleDTO
     */
    @Override
    public void updateSysUserRoleById(SysUserRoleDTO sysUserRoleDTO){
        //判断是否存在重复数据
        SysUserRoleDTO userRoleDTO = sysUserRoleMapper.getSysUserRoleByUserIdANDRoleId(sysUserRoleDTO);
        if(null != userRoleDTO && !userRoleDTO.getId().equals(sysUserRoleDTO.getId())){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_USERROLE);
        }
        sysUserRoleMapper.updateSysUserRoleById(sysUserRoleDTO);
    }

    /**
     * 根据id删除用户角色信息
     * @param id
     */
    @Override
    public void deleteSysUserRoleById(Long id){
        sysUserRoleMapper.deleteSysUserRoleById(id);
    }

    /**
     * 批量新增用户角色信息
     * @param sysUserRoleDTOList
     * @return
     */
    @Override
    public void saveSysUserRoleForBatch(List<SysUserRoleDTO> sysUserRoleDTOList){

        //判断传入的数据是否包含非同一个渠道下的数据，如果有则应提示数据非法
        for (int i = 0;i < sysUserRoleDTOList.size();i++){

            SysUserRoleDTO userRoleDTO = sysUserRoleDTOList.get(i);
            SysUserDTO userDTO = new SysUserDTO();
            userDTO.setId(userRoleDTO.getUserId());
            SysUserDTO userRes = sysUserMapper.getSysUser(userDTO);
            Preconditions.checkNotNull(userRes, "不存在UserID为： " + userRoleDTO.getUserId() + " 的用户");

            SysRoleDTO roleDTO = new SysRoleDTO();
            roleDTO.setId(userRoleDTO.getRoleId());
            SysRoleDTO roleRes = sysRoleMapper.getSysRoleById(roleDTO);
            Preconditions.checkNotNull(roleRes, "不存在RoleID为： " + userRoleDTO.getRoleId() + " 的角色");

        }

        List<SysUserRoleDTO> delParamList = new ArrayList<>();
        //清除传入参数中重复的userId值
        Map<Long, Integer> map = new HashMap<>();
        for(int i = 0;i<sysUserRoleDTOList.size();i++){
            SysUserRoleDTO sysUserRoleDTO = sysUserRoleDTOList.get(i);
            if(!map.containsKey(sysUserRoleDTO.getUserId())){
                delParamList.add(sysUserRoleDTO);
                map.put(sysUserRoleDTO.getUserId(), i);
            }
        }

        //数据传入后，首先清空当前用户所有角色，然后再新增用户角色数据
        sysUserRoleMapper.deleteSysUserRoleByUserIdForBatch(delParamList);

        //批量新增用户角色信息数据
        sysUserRoleMapper.saveSysUserRoleForBatch(sysUserRoleDTOList);
    }

}

package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysRoleMapper;
import com.qinghua.website.server.domain.SysRoleDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SysRoleServiceImpl
 * @Description 系统角色管理接口实现
 **/
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 根据用户ID查询角色信息
     * @param id
     * @return
     */
    @Override
    public SysRoleDTO getSysRoleById(Long id) {
        //获取session中登录用户的cust_code信息
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        sysRoleDTO.setId(id);

        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(sysRoleDTO);
        Preconditions.checkNotNull(resRole, "不存在RoleID为： " + sysRoleDTO.getId() + " 的角色");

        return sysRoleMapper.getSysRoleById(sysRoleDTO);
    }

    /**
     * 查询角色信息集合
     * @param sysRoleDTO
     * @return
     */
    @Override
    public List<SysRoleDTO> getSysRoleList(SysRoleDTO sysRoleDTO) {
        return sysRoleMapper.getSysRoleList(sysRoleDTO);
    }

    /**
     * 分页查询角色信息
     * @param sysRoleDTO
     * @return
     */
    @Override
    public PageInfo<SysRoleDTO> getSysRoleListByPage(SysRoleDTO sysRoleDTO){
        PageHelper.startPage(sysRoleDTO.getPageNum(),sysRoleDTO.getPageSize());
        List<SysRoleDTO> sysUserDTOList = sysRoleMapper.getSysRoleList(sysRoleDTO);
        return new PageInfo<>(sysUserDTOList);
    }

    /**
     * 新增角色信息
     * @param sysRoleDTO
     */
    @Override
    public void saveSysRole(SysRoleDTO sysRoleDTO){
        //校验角色名称是否存在
        SysRoleDTO roleDTO = sysRoleMapper.getSysRoleByRoleName(sysRoleDTO);
        if(null != roleDTO){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_ROLENAME);
        }
        //默认设置为启用,角色状态 1-启用，2-禁用
        sysRoleDTO.setRoleStatus("1");
        sysRoleMapper.saveSysRole(sysRoleDTO);
    }

    /**
     * 更新角色信息
     * @param sysRoleDTO
     */
    @Override
    public void updateSysRoleById(SysRoleDTO sysRoleDTO){
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(sysRoleDTO);
        Preconditions.checkNotNull(resRole, "不存在RoleID为： " + sysRoleDTO.getId() + " 的角色");

        if (StringUtils.isNoneBlank(sysRoleDTO.getRoleName())) {
            SysRoleDTO roleDTO = sysRoleMapper.getSysRoleByRoleName(sysRoleDTO);
            if (roleDTO != null && !roleDTO.getId().equals(sysRoleDTO.getId())) {
                throw new BizException(SysConstant.ERROR_USER_REPEAT_ROLENAME);
            }
        }

        sysRoleMapper.updateSysRoleById(sysRoleDTO);
    }

    /**
     * 根据id删除角色信息
     * @param sysRoleDTO
     */
    @Override
    public void deleteSysRoleById(SysRoleDTO sysRoleDTO){
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(sysRoleDTO);
        Preconditions.checkNotNull(resRole, "不存在RoleID为： " + sysRoleDTO.getId() + " 的角色");
        sysRoleMapper.deleteSysRoleById(sysRoleDTO);
    }

    /**
     * 根据用户ID查询角色信息
     * @param id
     * @return
     */
    @Override
    public List<String> getRoleListByUserId(Long id){
        return sysRoleMapper.getRoleListByUserId(id);
    }

    /**
     * 修改角色状态
     * @param sysRoleDTO
     * @return
     */
    @Override
    public void updateSysRoleStatusById(SysRoleDTO sysRoleDTO) {
        SysRoleDTO resRole = sysRoleMapper.getSysRoleById(sysRoleDTO);
        Preconditions.checkNotNull(resRole, "不存在RoleID为： " + sysRoleDTO.getId() + " 的角色");
        sysRoleMapper.updateSysRoleStatusById(sysRoleDTO);
    }

}

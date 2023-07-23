package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysPermissionMapper;
import com.qinghua.website.server.domain.SysPermissionDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.qinghua.website.server.utils.ListUtils.distinctByKey;

/**
 * @ClassName SysRoleServiceImpl
 * @Description 系统权限管理接口实现
 **/
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据用户ID查询权限信息
     * @param id
     * @return
     */
    @Override
    public SysPermissionDTO getSysPermissionById(Long id) {
        return sysPermissionMapper.getSysPermissionById(id);
    }

    /**
     * 查询权限信息集合
     * @param sysPermissionDTO
     * @return
     */
    @Override
    public List<SysPermissionDTO> getSysPermissionList(SysPermissionDTO sysPermissionDTO) {

        List<SysPermissionDTO> list = sysPermissionMapper.getSysPermissionList(sysPermissionDTO);
        //去除重复数据
        if(null != list && 0 != list.size()){
            list = list.stream().filter(distinctByKey(SysPermissionDTO::getId)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 分页查询权限信息
     * @param sysPermissionDTO
     * @return
     */
    @Override
    public PageInfo<SysPermissionDTO> getSysPermissionListByPage(SysPermissionDTO sysPermissionDTO){
        PageHelper.startPage(sysPermissionDTO.getPageNum(),sysPermissionDTO.getPageSize());
        List<SysPermissionDTO> sysPermissionDTOList = sysPermissionMapper.getSysPermissionList(sysPermissionDTO);
        return new PageInfo<>(sysPermissionDTOList);
    }

    /**
     * 新增权限信息
     * @param sysPermissionDTO
     */
    @Override
    public void saveSysPermission(SysPermissionDTO sysPermissionDTO){

        //判断同组内权限名称是否重复
        SysPermissionDTO permissionDTOName = sysPermissionMapper.getPermsByName(sysPermissionDTO);
        if(permissionDTOName != null){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_PERMSPATH);
        }
        //判断同组内权限路径是否重复
        SysPermissionDTO permissionDTOPath = sysPermissionMapper.getPermsByPath(sysPermissionDTO);
        if (permissionDTOPath != null){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_PERMSPATH);
        }

        sysPermissionMapper.saveSysPermission(sysPermissionDTO);

    }

    /**
     * 更新权限信息
     * @param sysPermissionDTO
     */
    @Override
    public void updateSysPermissionById(SysPermissionDTO sysPermissionDTO){
        SysPermissionDTO queryRes = sysPermissionMapper.getSysPermissionByIdAndGroup(sysPermissionDTO);
        if(null == queryRes){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_PERMSNAME_UNDEFIND);
        }

        //判断权限名称是否重复
        SysPermissionDTO permissionDTOName = sysPermissionMapper.getPermsByName(sysPermissionDTO);
        if(permissionDTOName != null && !sysPermissionDTO.getId().equals(permissionDTOName.getId())){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_PERMSNAME);
        }

        //判断权限路径是否重复
        SysPermissionDTO permissionDTOPath = sysPermissionMapper.getPermsByPath(sysPermissionDTO);
        if (permissionDTOPath != null && !sysPermissionDTO.getId().equals(permissionDTOPath.getId())){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_PERMSPATH);
        }

        sysPermissionMapper.updateSysPermissionById(sysPermissionDTO);

    }

    /**
     * 根据id删除权限信息
     * @param id
     */
    @Override
    public void deleteSysPermissionById(Long id){
        sysPermissionMapper.deleteSysPermissionById(id);
    }

    /**
     * 根据用户ID查询权限信息
     * @param id
     * @return
     */
    @Override
    public List<String> getPermsStringByUserId(Long id){
        return sysPermissionMapper.getPermsStringByUserId(id);
    }

    /**
     * 根据用户ID查询权限集合
     * @param userId
     * @return
     */
    @Override
    public List<SysPermissionDTO> getPermsListByUserId(Long userId){
        List<SysPermissionDTO> list = sysPermissionMapper.getPermsListByUserId(userId);
        //去除重复数据
        if (null != list && 0 != list.size()){
            list = list.stream().filter(distinctByKey(SysPermissionDTO::getId)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据角色ID查询权限信息
     * @param id
     * @return
     */
    @Override
    public List<SysPermissionDTO> getPermissionByRoleId(Long id){
        List<SysPermissionDTO> permissionList = sysPermissionMapper.getPermissionByRoleId(id);
        //去除重复数据
        if(null != permissionList && 0 != permissionList.size()){
            permissionList = permissionList.stream().filter(distinctByKey(SysPermissionDTO::getId)).collect(Collectors.toList());
        }
        return permissionList;
    }


    @Override
    public List<SysPermissionDTO> findAllListByGroup(String group) {
        return sysPermissionMapper.findAllListByGroup(group);
    }

}

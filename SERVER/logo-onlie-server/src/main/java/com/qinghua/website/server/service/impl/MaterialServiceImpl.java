package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.MaterialDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.MaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.MaterialMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 素材信息表 服务实现类
 */
@Service
public class MaterialServiceImpl implements MaterialService {

                
    @Resource
    private MaterialMapper materialMapper;

    @Override
    public List<MaterialDTO> getMaterialList(MaterialDTO bean) {
        return materialMapper.getMaterialList(bean);
    }

    @Override
    public PageInfo<MaterialDTO> getMaterialListByPage(MaterialDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<MaterialDTO> materialList = materialMapper.getMaterialList(bean);
        return PageInfo.of(materialList);
    }

    @Override
    public MaterialDTO getMaterialById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return materialMapper.getMaterialById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveMaterial(MaterialDTO bean) {
        materialMapper.saveMaterial(bean);
    }

    @Override
    public void deleteMaterialById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        materialMapper.deleteMaterialById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMaterialById(MaterialDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        materialMapper.updateMaterialById(bean);
    }

    @Override
    public void deleteMaterialByIdOSS(Long id){
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        materialMapper.deleteMaterialById(id);
    }

}

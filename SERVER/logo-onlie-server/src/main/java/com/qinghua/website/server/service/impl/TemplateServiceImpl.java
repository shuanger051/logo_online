package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.TemplateDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.TemplateMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 模板信息表 服务实现类
 */
@Service
public class TemplateServiceImpl implements TemplateService {
                
    @Resource
    private TemplateMapper templateMapper;

    @Override
    public List<TemplateDTO> getTemplateList(TemplateDTO bean) {
        return templateMapper.getTemplateList(bean);
    }

    @Override
    public PageInfo<TemplateDTO> getTemplateListByPage(TemplateDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<TemplateDTO> templateList = templateMapper.getTemplateList(bean);
        return PageInfo.of(templateList);
    }

    @Override
    public TemplateDTO getTemplateById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return templateMapper.getTemplateById(id);
    }

    @Override
    public TemplateDTO getTemplateByIdAPI(Long id){
        TemplateDTO res = templateMapper.getTemplateByIdAPI(id);
        if(null != res){
            return res;
        }else{
            throw new BizException("没有ID为："+id+"的模板数据", SysConstant.SYSTEM_ERROR_400.getCode());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveTemplate(TemplateDTO bean) {
        templateMapper.saveTemplate(bean);
    }

    @Override
    public void deleteTemplateById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        templateMapper.deleteTemplateById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateTemplateById(TemplateDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        templateMapper.updateTemplateById(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateTemplateStatusById(TemplateDTO templateDTO){
        templateMapper.updateTemplateStatusById(templateDTO);
    }

    /**
     * 分页查询模板信息列表
     * @param bean
     * @return
     */
    @Override
    public PageInfo<TemplateDTO> queryTemplateListPageAPI(TemplateDTO bean){
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<TemplateDTO> list = templateMapper.queryTemplateListPageAPI(bean);
        return new PageInfo<>(list);
    }

}

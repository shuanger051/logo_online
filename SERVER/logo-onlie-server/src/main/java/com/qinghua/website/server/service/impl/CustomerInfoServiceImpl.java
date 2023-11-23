package com.qinghua.website.server.service.impl;

import com.hazelcast.util.MD5Util;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.CustomerInfoDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.CustomerInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.server.utils.Sm4Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.CustomerInfoMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 客户信息表 服务实现类
 * @author Mybatis-Generator
 * @since 2023-07-17
 */
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService {

                
    @Resource
    private CustomerInfoMapper customerInfoMapper;

    @Override
    public List<CustomerInfoDTO> getCustomerInfoList(CustomerInfoDTO bean) {
        return customerInfoMapper.getCustomerInfoList(bean);
    }

    @Override
    public PageInfo<CustomerInfoDTO> getCustomerInfoListByPage(CustomerInfoDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<CustomerInfoDTO> customerInfoList = customerInfoMapper.getCustomerInfoList(bean);
        return PageInfo.of(customerInfoList);
    }

    @Override
    public CustomerInfoDTO getCustomerInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return customerInfoMapper.getCustomerInfoById(id);
    }

    @Override
    public void saveCustomerInfo(CustomerInfoDTO bean) {
        //校验customerName是否重复
        CustomerInfoDTO res = customerInfoMapper.getCustomerInfoByCustomerName(bean.getCustomerName());
        if(null == res){
            customerInfoMapper.saveCustomerInfo(bean);
        }else{
            throw new BizException("客户名称：{} 的账号已存在，请修改账号信息!", SysConstant.SYSTEM_ERROR_400.getCode());
        }
    }

    @Override
    public void deleteCustomerInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        customerInfoMapper.deleteCustomerInfoById(id);
    }

    @Override
    public CustomerInfoDTO getCustomerByCustomerName(String customerName) {
        return customerInfoMapper.getCustomerInfoByCustomerName(customerName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateCustomerInfoById(CustomerInfoDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        customerInfoMapper.updateCustomerInfoById(bean);
    }

    /**
     * 修改客户密码
     * @param id
     * @param oldPwd
     * @param newPwd
     */
    @Override
    public void updateCustomerPwdByID(Long id,String oldPwd,String newPwd){

        CustomerInfoDTO param = new CustomerInfoDTO();
        param.setId(id);
        param.setPassword(MD5Util.toMD5String(oldPwd));

        //校验旧密码是否正确
        CustomerInfoDTO check = customerInfoMapper.getCustomerByIDPwd(param);
        if(null != check){
            param.setPassword(MD5Util.toMD5String(newPwd));
            customerInfoMapper.updateCustomerPwdByID(param);
        }else{
            throw new BizException("旧密码错误",SysConstant.SYSTEM_ERROR_400.getCode());
        }
    }

    /**
     * 根据账号和密码校验账户信息
     * @param customerName
     * @param pwd
     * @return
     */
    @Override
    public CustomerInfoDTO checkCustomerByPWDAndAccount(String customerName,String pwd){
        CustomerInfoDTO res = customerInfoMapper.checkCustomerByPWDAndAccount(customerName,pwd);
        return res;
    }

}

package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.CustomerInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 客户信息表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-07-17
 */
public interface CustomerInfoService {

        /**
        * 分页查询
        */
        PageInfo<CustomerInfoDTO> getCustomerInfoListByPage(CustomerInfoDTO bean);

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<CustomerInfoDTO> getCustomerInfoList(CustomerInfoDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        CustomerInfoDTO getCustomerInfoById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveCustomerInfo(CustomerInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateCustomerInfoById(CustomerInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteCustomerInfoById(Long id);

        /**
         * 根据账户名称查询客户信息
         * @param customerName
         * @return
         */
        CustomerInfoDTO getCustomerByCustomerName(String customerName);

        /**
         * 修改客户密码
         * @param id
         * @param oldPwd
         * @param newPwd
         */
        void updateCustomerPwdByID(Long id,String oldPwd,String newPwd);

        /**
         * 根据账号和密码校验账户信息
         * @param customerName
         * @param pwd
         * @return
         */
        CustomerInfoDTO checkCustomerByPWDAndAccount(String customerName,String pwd);

}

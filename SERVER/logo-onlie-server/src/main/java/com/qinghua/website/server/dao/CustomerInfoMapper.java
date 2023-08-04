package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.CustomerInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 客户信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-07-17
 */
@Mapper
public interface CustomerInfoMapper {

        /**
         * 查询列表集合
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
        CustomerInfoDTO getCustomerInfoById(@Param("id") Long id);

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
        void deleteCustomerInfoById(@Param("id") Long id);

        /**
         * 根据客户名称查询客户信息
         * @param customerName
         * @return
         */
        CustomerInfoDTO getCustomerInfoByCustomerName(@Param("customerName")String customerName);

        /**
         * 根据密码查询客户信息
         * @param customerInfoDTO
         * @return
         */
        CustomerInfoDTO getCustomerByIDPwd(CustomerInfoDTO customerInfoDTO);

        /**
         * 修改客户密码
         * @param customerInfoDTO
         */
        void updateCustomerPwdByID(CustomerInfoDTO customerInfoDTO);

        /**
         * 根據账号密码校验账号有效性
         * @param customerName
         * @param pwd
         * @return
         */
        CustomerInfoDTO checkCustomerByPWDAndAccount(@Param("customerName") String customerName,@Param("pwd") String pwd);

}
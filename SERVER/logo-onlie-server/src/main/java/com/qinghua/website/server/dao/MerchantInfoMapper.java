package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.MerchantInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface MerchantInfoMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<MerchantInfoDTO> getMerchantInfoList(MerchantInfoDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        MerchantInfoDTO getMerchantInfoById(@Param("id") Long id);

        /**
         * 根据身份证号查询商户信息
         * @param idCard
         * @return
         */
        MerchantInfoDTO getMerchantInfoByIdCard(@Param("idCard")String idCard);

        /**
         * 新增
         * @param bean
         */
        Integer saveMerchantInfo(MerchantInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateMerchantInfoById(MerchantInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteMerchantInfoById(@Param("id") Long id);

}
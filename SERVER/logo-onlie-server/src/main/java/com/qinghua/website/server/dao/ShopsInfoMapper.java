package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ShopsInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ShopsInfoMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ShopsInfoDTO> getShopsInfoList(ShopsInfoDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ShopsInfoDTO getShopsInfoById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveShopsInfo(ShopsInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateShopsInfoById(ShopsInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteShopsInfoById(@Param("id") Long id);

        /**
         * 根据商户ID查询商铺信息集合
         * @param merchantId
         * @return
         */
        List<ShopsInfoDTO> getShopsInfoByMerchantId(@Param("merchantId")Long merchantId);

        /**
         * 修改商铺备案状态
         * @param bean
         */
        void updateShopsFilingsStatusAPI(ShopsInfoDTO bean);

}
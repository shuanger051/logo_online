package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.MerchantInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface MerchantInfoService {

        /**
        * 分页查询
        */
        PageInfo<MerchantInfoDTO> getMerchantInfoListByPage(MerchantInfoDTO bean);

        /**
         * 查询List集合
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
        MerchantInfoDTO getMerchantInfoById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveMerchantInfo(MerchantInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateMerchantInfoById(MerchantInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteMerchantInfoById(Long id);

        /**
         * 根据身份证号查询商户信息
         * @param idCard
         * @return
         */
        MerchantInfoDTO getMerchantInfoByIdCard(String idCard);

}

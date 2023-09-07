package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ShopsAttachmentDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ShopsInfoService {

        /**
        * 分页查询
        */
        PageInfo<ShopsInfoDTO> getShopsInfoListByPage(ShopsInfoDTO bean);

        /**
         * 查询List集合
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
        ShopsInfoDTO getShopsInfoById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveShopsInfo(ShopsInfoDTO bean,List<ShopsAttachmentDTO> list);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateShopsInfoById(ShopsInfoDTO bean,List<ShopsAttachmentDTO> list);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteShopsInfoById(Long id);

        /**
         * 根据附件名称删除附件
         * @param attachmentName
         */
        void deleteAttachmentByName(String attachmentName);

        /**
         * 根据商户ID查询店铺信息
         * @param merchantId
         * @return
         */
        List<ShopsInfoDTO> getShopsInfoByMerchantId(Long merchantId);

        /**
         * 保存店铺附件
         * @param list
         */
        void saveShopsAttachments(List<ShopsAttachmentDTO> list);

        /**
         * 修改店铺备案状态
         * @param bean
         */
        void updateShopsFilingsStatusAPI(ShopsInfoDTO bean);

        /**
         * 根据商铺ID获取身份证正反面信息
         * @param shopsId
         * @return
         */
        ShopsInfoDTO getShopsInfoByIdAPI(Long shopsId);

        /**
         * 根据ID查询身份证信息
         * @param id
         * @return
         */
        ShopsInfoDTO getShopsIdCardByIdAPI(Long id);

}

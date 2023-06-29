package com.qinghua.website.server.service;

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
        Integer deleteShopsInfoById(Long id);

}

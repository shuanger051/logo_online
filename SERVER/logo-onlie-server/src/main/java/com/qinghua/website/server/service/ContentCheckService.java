package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ContentCheckDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 内容审核信息表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ContentCheckService {

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<ContentCheckDTO> getContentCheckList(ContentCheckDTO bean);

        /**
         * 根据ContentID查询
         *
         * @param contentId
         * @return
         */
        ContentCheckDTO getContentCheckByContentId(Long contentId);

        /**
         * 根据content_id更新审核信息
         * @param checkDTO
         * @return
         */
        void updateContentCheckByContentId(ContentCheckDTO checkDTO);

        /**
         * 新增
         * @param bean
         */
        Integer saveContentCheck(ContentCheckDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentCheckById(ContentCheckDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentCheckById(Long id);

}

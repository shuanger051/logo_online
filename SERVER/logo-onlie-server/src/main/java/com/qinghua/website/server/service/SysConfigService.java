package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.SysConfigDTO;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 */
public interface SysConfigService {

        /**
         * 根据ID查询
         * @param key
         * @return
         */
        SysConfigDTO getSysConfigByKey(String key);
}

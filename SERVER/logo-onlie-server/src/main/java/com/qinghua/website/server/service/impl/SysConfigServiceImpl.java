package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.SysConfigDTO;
import com.qinghua.website.server.service.SysConfigService;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import com.qinghua.website.server.dao.SysConfigMapper;
import javax.annotation.Resource;

/**
 * 系统配置表 服务实现类
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

                
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public SysConfigDTO getSysConfigByKey(String key) {
        Preconditions.checkNotNull(key, "参数:config_key不能为空");
        return sysConfigMapper.getSysConfigByKey(key);
    }
}

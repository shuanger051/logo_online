package com.qinghua.website.api.task;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysConfigDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.LogInfoService;
import com.qinghua.website.server.service.SysConfigService;
import com.qinghua.website.server.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 数据库定时任务执行器
 */
@Slf4j
@Component
public class DBTask {

    //数据库配置的数据保存天数的KEY
    private static final String CLEAR_DAYS_KEY = "log_storge_days";

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private LogInfoService logInfoService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 每日凌晨1点执行一次删除日志保存超过60天的数据记录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearLogInfoThen60Days(){
        log.info("[消息：]系统任务开始清除历史日志数据……");
        //优先从redis中获取参数，若redis中没有此配置，则从数据库读取后再存入redis;
        String redisKeysVal = redisUtil.get(CLEAR_DAYS_KEY);
        if(null == redisKeysVal){
            SysConfigDTO sysConfigDTO = sysConfigService.getSysConfigByKey(CLEAR_DAYS_KEY);
            if(null != sysConfigDTO){
                redisKeysVal = sysConfigDTO.getConfigValue();
                redisUtil.set(CLEAR_DAYS_KEY,redisKeysVal,365*3, TimeUnit.DAYS);
            }else{
                log.error("[错误消息：]表t_sys_config中未找到配置参数为{}的KEY",CLEAR_DAYS_KEY);
                throw new BizException(SysConstant.ERROR_SYS_CONFIG_KEY_IS_NOT_EXIST);
            }
        }
        //根据配置的天数查询满足条件的日志数据，并执行物理删除
        logInfoService.deleteLogInfoListByDays(Integer.parseInt(redisKeysVal));
        log.info("[消息：]日志任务执行成功，已清除超过{}天的历史数据！",redisKeysVal);
    }

}

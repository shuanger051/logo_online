package com.qinghua.website.api.listener;

import com.alibaba.fastjson.JSON;
import com.qinghua.website.server.domain.SysDictItemDTO;
import com.qinghua.website.server.enums.DictEnum;
import com.qinghua.website.server.service.SysDictItemService;
import com.qinghua.website.server.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用启动监听
 */
@Slf4j
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private SysDictItemService sysDictItemService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动监听线程
        log.info("应用启动成功!");
        log.info("将所有的字典项加载到redis中……");
        // 先删除redis中的已经存在的配置
        redisUtil.delete(DictEnum.REDIS_MAP_KEY.getCode());
        Map<String, Object> map = new HashMap<>();
        // 去掉分页

        SysDictItemDTO sysDictItemDTO = new SysDictItemDTO();
        List<SysDictItemDTO> dictItemList = sysDictItemService.getDictItemList(sysDictItemDTO);
        Map<String, List<SysDictItemDTO>> collect = dictItemList.stream().collect(Collectors.groupingBy(SysDictItemDTO::getDictKey));
        Iterator<String> iterator = collect.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            HashMap<String, Object> subMap = new HashMap<>();
            List<SysDictItemDTO> sysDictItemDTOS = collect.get(key);
            sysDictItemDTOS.forEach(dict -> subMap.put(dict.getItemKey(), dict.getItemValue()));
            map.put(key, JSON.toJSONString(subMap));
        }
        redisUtil.hPutAll(DictEnum.REDIS_MAP_KEY.getCode(), map);


    }

}

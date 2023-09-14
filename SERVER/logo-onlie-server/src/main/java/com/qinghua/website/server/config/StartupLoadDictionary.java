package com.qinghua.website.server.config;

import com.alibaba.fastjson.JSON;
import com.qinghua.website.server.domain.SysDictItemDTO;
import com.qinghua.website.server.enums.DictEnum;
import com.qinghua.website.server.service.SysDictItemService;
import com.qinghua.website.server.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StartupLoadDictionary implements CommandLineRunner {

    @Resource
    private SysDictItemService sysDictItemService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) {
        log.info("项目启动成功，将所有的字典项加载到redis中……");
        // 先删除redis中的已经存在的配置
        redisUtil.delete(DictEnum.REDIS_MAP_KEY.getCode());
        Map<String, Object> map = new HashMap<>();
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
        redisUtil.expire(DictEnum.REDIS_MAP_KEY.getCode(),365*3, TimeUnit.DAYS);
        log.info("[消息：] Redis字典缓存成功!");
    }

}

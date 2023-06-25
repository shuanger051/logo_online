package com.qinghua.website.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.qinghua.website.server.enums.DictEnum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 字典工具类
 */
@Component
public class DictUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 通过dictKey获取DictItems
     * @param dictKey
     * @return
     */
    public Map<String, Object> getDictItemsByDictKey(String dictKey) {
        String result = (String)stringRedisTemplate.opsForHash().get(DictEnum.REDIS_MAP_KEY.getCode(), dictKey);
        if (result == null) {
            return new HashMap<>();
        }
        Map<String,Object> map = JSONObject.parseObject(result, Map.class);
        return map;
    }
}

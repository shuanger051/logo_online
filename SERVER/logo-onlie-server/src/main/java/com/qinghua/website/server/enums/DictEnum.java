package com.qinghua.website.server.enums;

import com.qinghua.website.server.constant.IEnum;

/**
 * @author yzhang
 * @date 2020/11/4
 * 字典项的相关配置
 */
public enum DictEnum implements IEnum {

    REDIS_MAP_KEY("dictionaryMap","存放redis的map的key值"),
    MODEL_TYPE("model_type","物模型类型");

    private final String code;

    private final String msg;

    DictEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

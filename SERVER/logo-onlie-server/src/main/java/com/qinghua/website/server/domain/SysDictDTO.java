package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class SysDictDTO extends BaseDTO {

    /**
     * id:
     */
    private Long id;

    /**
     *字典类型编码
     */
    private String dictKey;

    /**
     *字典类型名称
     */
    private String dictName;

    @Override
    public String toString() {
        return "SysDictDTO{" +
                "dictKey='" + dictKey + '\'' +
                ", dictName='" + dictName + '\'' +
                '}';
    }

}

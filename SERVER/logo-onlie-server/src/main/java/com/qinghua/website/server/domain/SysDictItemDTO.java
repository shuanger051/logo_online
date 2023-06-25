package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class SysDictItemDTO extends BaseDTO {

    /**
     * id:
     */
    private Long id;

    /**
     * 序号
     */
    private Integer itemSort;

    /**
     * 数据字典KEY
     */
    private String itemKey;

    /**
     * 数据字典值
     */
    private String itemValue;

    /**
     * 字典类型编码
     */
    private String dictKey;

    @Override
    public String toString() {
        return "SysDictItemDTO{" +
                "itemSort=" + itemSort +
                ", itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", dictKey='" + dictKey + '\'' +
                '}';
    }

}

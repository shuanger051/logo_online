package com.qinghua.website.mobile.controller.vo;

import lombok.Data;

/**
 * @ClassName SysDictItemVO
 * @Description 数据字典条目查询VO
 **/
@Data
public class SysDictItemVO {

    /**
     *主键ID
     */
    private Long id;

    /**
     *序号
     */
    private Integer itemSort;

    /**
     *数据字典KEY
     */
    private String itemKey;

    /**
     *数据字典值
     */
    private String itemValue;

    /**
     *字典类型编码
     */
    private String dictKey;

    @Override
    public String toString() {
        return "SysDictItemVO{" +
                "id=" + id +
                ", itemSort=" + itemSort +
                ", itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", dictKey='" + dictKey + '\'' +
                '}';
    }

}

package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysDictItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName DictItemMapper
 * @Description 数据字典条目mapper
 **/
@Mapper
public interface SysDictItemMapper {

    /**
     * 数据字典数据查询
     *
     * @param sysDictItemDTO
     * @return
     */
    List<SysDictItemDTO> getDictItemList(SysDictItemDTO sysDictItemDTO);

    /**
     * 根据ID查询数据字典条目
     *
     * @param id
     * @return
     */
    SysDictItemDTO getDictItemById(Long id);

    /**
     * 数据数据字典条目新增
     *
     * @param dictItemDTO
     * @return
     */
    void saveDictItem(SysDictItemDTO dictItemDTO);

    /**
     * 数据字典条目修改
     *
     * @param dictItemDTO
     * @return
     */
    void updateDictItemById(SysDictItemDTO dictItemDTO);

    /**
     * 数据字典条目数据删除
     *
     * @param id
     */
    Integer deleteDictItemById(Long id);

    /**
     * 根据数据字典类型Key批量删除数据字典条目数据
     * @param dictKey
     */
    void deleteDictItemByDictId(@Param("dictKey") String dictKey);

    /**
     * 查询数据字典子项编码或数据字典字典名称是否存在
     * @param dictItemDTO
     * @return
     */
    List<SysDictItemDTO> getSysDictItemByItemKeyOrItemValueList(SysDictItemDTO dictItemDTO);

    /**
     * 删除之后修改排序
     * @param item
     */
    void updateSort(SysDictItemDTO item);

    /**
     * 返回字典列表
     * @param dictKey
     * @return
     */
    List<SysDictItemDTO> getItemsByDictKeyInDB(@Param("dictKey") String dictKey);
}

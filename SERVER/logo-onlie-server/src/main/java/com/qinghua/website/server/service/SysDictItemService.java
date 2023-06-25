package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysDictItemDTO;

import java.util.List;

public interface SysDictItemService {

    /**
     * 数字字典条目分页查询
     * @param sysDictItemDTO
     * @return
     */
    PageInfo<SysDictItemDTO> getDictItemListByPage(SysDictItemDTO sysDictItemDTO);

    /**
     * 数据字典数据查询
     * @param sysDictItemDTO
     * @return
     */
    List<SysDictItemDTO> getDictItemList(SysDictItemDTO sysDictItemDTO);

    /**
     * 数据字典条目数据查看
     * @param id
     * @return
     */
    SysDictItemDTO getDictItemById(Long id);

    /**
     * 数据字典条目数据添加
     * @param dictItem
     * @return
     */
    void saveDictItem(SysDictItemDTO dictItem);

    /**
     * 数据字典条目数据修改
     * @param dictItem
     * @return
     */
    void updateDictItemById(SysDictItemDTO dictItem);

    /**
     * 数据字典条目数据删除
     * @param id
     * @return
     */
    void deleteDictItemById(Long id);

    /**
     * 根据数据字典类型批量删除数据字典条目数据
     * @param dictId
     */
    void deleteDictItemByDictKey(String dictId);

    /**
     * 返回字典列表
     * @param dictKey
     * @return
     */
    List<SysDictItemDTO> getItemsByDictKey(String dictKey);

}

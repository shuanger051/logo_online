package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysDictDTO;

import java.util.List;

/**
 * @ClassName DictService
 * @Description 数据字典接口
 **/
public interface SysDictService {


    /**
     * 分页查询
     * @param sysDictDTO
     * @return
     */
    PageInfo<SysDictDTO> getDictListByPage(SysDictDTO sysDictDTO);

    /**
     * 数据字典查询
     * @param sysDictDTO
     * @return
     */
    List<SysDictDTO> getDictList(SysDictDTO sysDictDTO);

    /**
     * 数据字典类型添加
     * @param sysDictDTO
     * @return
     */
    void saveDict(SysDictDTO sysDictDTO);

    /**
     * 数据字典类型查看
     * @param id
     * @return
     */
    SysDictDTO getDictById(Long id);

    /**
     * 修改数据字典类型信息
     * @param sysDictDTO
     * @return
     */
    void updateDictById(SysDictDTO sysDictDTO);

    /**
     * 删除数据字典类型信息
     * @param id
     * @return
     */
    void deleteDictById(Long id);
}

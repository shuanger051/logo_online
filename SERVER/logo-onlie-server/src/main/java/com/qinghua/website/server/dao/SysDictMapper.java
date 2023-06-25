package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysDictDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName DictMapper
 * @Description 数据字典类型mapper
 **/
@Repository
public interface SysDictMapper {

    /**
     * 数据字典查询
     * @param sysDictDTO
     * @return
     */
    List<SysDictDTO> getDictList(SysDictDTO sysDictDTO);

    /**
     * 根据ID数据字典类型查询
     * @param id
     * @return
     */
    SysDictDTO getDictById(Long id);

    /**
     * 数据字典类型新增
     * @param dictDTO
     * @return
     */
    void saveDict(SysDictDTO dictDTO);

    /**
     * 数据字典类型修改
     * @param dictDTO
     * @return
     */
    void updateDictById(SysDictDTO dictDTO);

    /**
     * 数据字典类型删除
     * @param id
     */
    void deleteDictById(Long id);

    /**
     * 查询数据字典编码或数据字典名称是否存在
     * @param dictDTO
     * @return
     */
    List<SysDictDTO> getSysDictByDictKeyOrDictNameList(SysDictDTO dictDTO);
}

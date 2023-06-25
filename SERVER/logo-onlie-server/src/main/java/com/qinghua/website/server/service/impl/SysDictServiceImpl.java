package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysDictMapper;
import com.qinghua.website.server.domain.SysDictDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysDictItemService;
import com.qinghua.website.server.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName DictServiceImpl
 * @Description 数据字典类型接口实现
 **/
@Slf4j
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    private SysDictItemService sysDictItemService;


    /**
     * 数据字典分页查询
     *
     * @param sysDictDTO
     * @return
     */
    @Override
    public PageInfo<SysDictDTO> getDictListByPage(SysDictDTO sysDictDTO) {
        PageHelper.startPage(sysDictDTO.getPageNum(), sysDictDTO.getPageSize());
        List<SysDictDTO> sysDictList = sysDictMapper.getDictList(sysDictDTO);
        return PageInfo.of(sysDictList);
    }

    /**
     * 数据字典查询
     *
     * @param sysDictDTO
     * @return
     */
    @Override
    public List<SysDictDTO> getDictList(SysDictDTO sysDictDTO) {
        return sysDictMapper.getDictList(sysDictDTO);
    }

    /**
     * 数据字典类型新增
     *
     * @param dictDTO
     * @return
     */
    @Override
    public void saveDict(SysDictDTO dictDTO) {
        List<SysDictDTO> dictList = sysDictMapper.getSysDictByDictKeyOrDictNameList(dictDTO);
        dictList.forEach(e -> {
            if (e.getDictKey().equals(dictDTO.getDictKey())) {
                throw new BizException(SysConstant.ERROR_DICT_CHECK_DICTKEY_10007);
            }
            if (e.getDictName().equals(dictDTO.getDictName())) {
                throw new BizException(SysConstant.ERROR_DICT_CHECK_DICTNAME_10006);
            }
        });
        sysDictMapper.saveDict(dictDTO);
    }

    /**
     * 数据字典类型查看
     *
     * @param id
     * @return
     */
    @Override
    public SysDictDTO getDictById(Long id) {
        SysDictDTO dict = sysDictMapper.getDictById(id);
        return dict;
    }

    /**
     * 修改数据字典类型信息
     *
     * @param dictDTO
     * @return
     */
    @Override
    public void updateDictById(SysDictDTO dictDTO) {

        SysDictDTO dict = new SysDictDTO();
        dict.setDictName(dictDTO.getDictName());
        List<SysDictDTO> dictList = sysDictMapper.getDictList(dict);
        dictList.forEach(e -> {
            if (!e.getId().equals(dictDTO.getId())) {
                throw new BizException(SysConstant.ERROR_DICT_CHECK_DICTNAME_10006);
            }
        });
        sysDictMapper.updateDictById(dictDTO);
    }

    /**
     * 删除数据字典类型信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteDictById(Long id) {
        SysDictDTO dict = sysDictMapper.getDictById(id);
        if (dict != null) {
            // 删除当前数据
            sysDictMapper.deleteDictById(id);
            // 根据dictKey删除字典子项列表
            sysDictItemService.deleteDictItemByDictKey(dict.getDictKey());
        }
    }
}

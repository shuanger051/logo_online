package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysDictItemMapper;
import com.qinghua.website.server.domain.SysDictDTO;
import com.qinghua.website.server.domain.SysDictItemDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysDictItemService;
import com.qinghua.website.server.service.SysDictService;
import com.qinghua.website.server.utils.DictUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DictItemServiceImpl
 * @Description 数据字典条目接口实现
 **/
@Slf4j
@Service
public class SysDictItemServiceImpl implements SysDictItemService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Autowired
    private SysDictService sysDictService;

    @Resource
    private DictUtil dictUtil;

    /**
     * 数据字典分页查询
     *
     * @param sysDictItemDTO
     * @return
     */
    @Override
    public PageInfo<SysDictItemDTO> getDictItemListByPage(SysDictItemDTO sysDictItemDTO) {
        PageHelper.startPage(sysDictItemDTO.getPageNum(), sysDictItemDTO.getPageSize());
        List<SysDictItemDTO> sysDictItemList = sysDictItemMapper.getDictItemList(sysDictItemDTO);
        return PageInfo.of(sysDictItemList);
    }

    /**
     * 数据字典数据查询
     *
     * @param sysDictItemDTO
     * @return
     */
    @Override
    public List<SysDictItemDTO> getDictItemList(SysDictItemDTO sysDictItemDTO) {
        return sysDictItemMapper.getDictItemList(sysDictItemDTO);
    }

    /**
     * 数据字典条目数据查看
     *
     * @param id
     * @return
     */
    @Override
    public SysDictItemDTO getDictItemById(Long id) {
        SysDictItemDTO dictItemDTO = sysDictItemMapper.getDictItemById(id);
        return dictItemDTO;
    }

    /**
     * 数据字典条目数据添加
     *
     * @param dictItemDTO
     * @return
     */
    @Override
    public void saveDictItem(SysDictItemDTO dictItemDTO) {

        if (!checkDictKeyExists(dictItemDTO.getDictKey())) {
            throw new BizException(SysConstant.ERROR_DICT_CHECK_DICTKEY_10008);
        }
         // 去除分页效果
        SysDictItemDTO dto = new SysDictItemDTO();
        dto.setDictKey(dictItemDTO.getDictKey());
        List<SysDictItemDTO> dictItemList = sysDictItemMapper.getDictItemList(dto);
        if (dictItemList.size() > 0) {
            List<SysDictItemDTO> sysDictItemList = sysDictItemMapper.getSysDictItemByItemKeyOrItemValueList(dictItemDTO);
            sysDictItemList.forEach(e -> {
                if (e.getItemKey().equals(dictItemDTO.getItemKey())) {
                    throw new BizException(SysConstant.ERROR_DICT_CHECK_ITEMKEY_10009);
                }
                if (e.getItemValue().equals(dictItemDTO.getItemValue())) {
                    throw new BizException(SysConstant.ERROR_DICT_CHECK_ITEMVALUE_10010);
                }
            });
        }
        // 设置排序编号
        dictItemDTO.setItemSort(dictItemList.size() + 1);
        sysDictItemMapper.saveDictItem(dictItemDTO);
    }


    /**
     * 判断dictKey是否存在
     *
     * @param dictKey
     * @return
     */
    private boolean checkDictKeyExists(String dictKey) {
        SysDictDTO sysDictDTO = new SysDictDTO();
        sysDictDTO.setDictKey(dictKey);
        return sysDictService.getDictList(sysDictDTO).size() > 0;
    }

    /**
     * 数据字典条目数据修改
     *
     * @param dictItemDTO
     * @return
     */
    @Override
    public void updateDictItemById(SysDictItemDTO dictItemDTO) {

        SysDictItemDTO dictItemById = sysDictItemMapper.getDictItemById(dictItemDTO.getId());
        Preconditions.checkNotNull(dictItemById, "数据字典不存在");
        dictItemDTO.setDictKey(dictItemById.getDictKey());
        List<SysDictItemDTO> sysDictItemList = sysDictItemMapper.getSysDictItemByItemKeyOrItemValueList(dictItemDTO);
        sysDictItemList.forEach(e -> {
            if (!e.getId().equals(dictItemDTO.getId())) {
                if (e.getItemKey().equals(dictItemDTO.getItemKey())) {
                    throw new BizException(SysConstant.ERROR_DICT_CHECK_ITEMKEY_10009);
                }
                if (e.getItemValue().equals(dictItemDTO.getItemValue())) {
                    throw new BizException(SysConstant.ERROR_DICT_CHECK_ITEMVALUE_10010);
                }
            }
        });
        sysDictItemMapper.updateDictItemById(dictItemDTO);
    }

    /**
     * 数据字典条目数据删除
     *
     * @param id
     * @return
     */
    @Override
    public void deleteDictItemById(Long id) {
        SysDictItemDTO item = sysDictItemMapper.getDictItemById(id);
        if (item != null) {
            Integer integer = sysDictItemMapper.deleteDictItemById(id);
            if (integer == 1) { // 删除成功更新排序
                sysDictItemMapper.updateSort(item);
            }
        }
    }

    /**
     * 根据数据字典类型批量删除数据字典条目数据
     * @param dictKey
     */
    @Override
    public void deleteDictItemByDictKey(String dictKey) {
        sysDictItemMapper.deleteDictItemByDictId(dictKey);
    }

    @Override
    public List<SysDictItemDTO> getItemsByDictKey(String dictKey) {
        Map<String, Object> dictItemsByDictKey = dictUtil.getDictItemsByDictKey(dictKey);
        List<SysDictItemDTO> result = new ArrayList<>();
        dictItemsByDictKey.entrySet().forEach(e -> {
            SysDictItemDTO dto = new SysDictItemDTO();
            dto.setItemKey(e.getKey());
            dto.setItemValue(e.getValue().toString());
            result.add(dto);
        });
        return result;
    }
}

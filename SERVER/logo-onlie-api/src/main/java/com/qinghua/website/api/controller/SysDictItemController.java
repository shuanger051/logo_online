package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.io.SysDictItemQueryIO;
import com.qinghua.website.api.controller.io.SysDictItemSaveIO;
import com.qinghua.website.api.controller.io.SysDictItemUpdateIO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysDictItemVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysDictItemDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysDictItemService;
import com.qinghua.website.server.service.SysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName DictItem
 * @Description 数据字典类型条目
 **/
@RestController
@RequestMapping("/sys/dict-item")
public class SysDictItemController {

    @Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 数据字典子项分页查询
     * @param sysDictItemQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "数据字典子项分页查询")
    @RequestMapping(value = "/getDictItemListByPage", method = RequestMethod.GET)
    public ResponseResult<Object> getDicItemtListPage(@Valid @RequestBody SysDictItemQueryIO sysDictItemQueryIO) {

        SysDictItemDTO sysDictItemDTO =  BeanToolsUtil.copyOrReturnNull(sysDictItemQueryIO, SysDictItemDTO.class);
        PageInfo<SysDictItemDTO> pageList = sysDictItemService.getDictItemListByPage(sysDictItemDTO);
        List<SysDictItemVO>  sysDictItemList = BeanToolsUtil.copyAsList(pageList.getList(), SysDictItemVO.class);

        PageListVO<SysDictItemVO> result = new PageListVO<>();
        result.setList(sysDictItemList);
        result.setTotal(pageList.getTotal());

        return ResponseResult.success(result);
    }

    /**
     * 数据字典通过dict_key查询字典子项列表
     * @param dictKey
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "数据字典通过dict_key查询字典子项列表")
    @RequestMapping(value = "/getItemsByDictKey", method = RequestMethod.GET)
    public ResponseResult<Object> getItemsByDictKey(@RequestParam("dictKey") String dictKey) {
        List<SysDictItemDTO> sysDictItemList = sysDictItemService.getItemsByDictKey(dictKey);
        List<SysDictItemVO> sysDictItemVOList = BeanToolsUtil.copyAsList(sysDictItemList,SysDictItemVO.class);
        return ResponseResult.success(sysDictItemVOList);
    }


    /**
     * 数据字典条目子项数据查看
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "数据字典条目子项数据查看")
    @RequestMapping(value = "/getDictItemById", method = RequestMethod.GET)
    public ResponseResult<Object> getDictItemById(@RequestParam("id") Long id) {
        SysDictItemDTO dictItem = sysDictItemService.getDictItemById(id);
        SysDictItemVO sysDictItemVO = BeanToolsUtil.copyOrReturnNull(dictItem, SysDictItemVO.class);
        return ResponseResult.success(sysDictItemVO);
    }


    /**
     * 数据字典子项添加
     * @param sysDictItemSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "数据字典子项添加")
    @RequestMapping(value = "/saveDictItem", method = RequestMethod.POST)
    public ResponseResult<Object> saveDictItem(@Valid @RequestBody SysDictItemSaveIO sysDictItemSaveIO) {
        if (!sysDictService.checkDictKeyExists(sysDictItemSaveIO.getDictKey())) {
            throw new BizException(SysConstant.ERROR_DICT_CHECK_DICTKEY_10008);
        }
        SysDictItemDTO dictItem = BeanToolsUtil.copyOrReturnNull(sysDictItemSaveIO, SysDictItemDTO.class);
        sysDictItemService.saveDictItem(dictItem);
        return ResponseResult.success();
    }

    /**
     * 数据字典子项数据修改
     * @param sysDictItemUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "数据字典子项数据修改")
    @RequestMapping(value = "/updateDictItemById", method = RequestMethod.POST)
    public ResponseResult<Object> updateDictItemById(@Valid @RequestBody SysDictItemUpdateIO sysDictItemUpdateIO) {
        SysDictItemDTO dictItem =  BeanToolsUtil.copyOrReturnNull(sysDictItemUpdateIO, SysDictItemDTO.class);
        sysDictItemService.updateDictItemById(dictItem);
        return ResponseResult.success();
    }

    /**
     * 数据字典子项数据删除
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "数据字典子项数据删除")
    @RequestMapping(value = "/deleteDictItemById", method = RequestMethod.POST)
    public ResponseResult<Object> deleteDictItemById(@Valid @RequestBody IdIO idIO) {
        sysDictItemService.deleteDictItemById(idIO.getId());
        return ResponseResult.success();
    }
}

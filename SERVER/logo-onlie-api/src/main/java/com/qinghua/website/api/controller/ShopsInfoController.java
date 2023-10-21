package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.ShopsAttachmentVO;
import com.qinghua.website.api.controller.vo.ShopsInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.utils.ImgUtils;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.ShopsAttachmentDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.service.ShopsInfoService;
import com.qinghua.website.server.utils.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shops-info")
public class ShopsInfoController {

    @Autowired
    private ShopsInfoService shopsInfoService;

    @Value("${uploadPath.urlPath}")
    private String urlPath;   //图标映射路径

    /**
     * 分页查询店铺信息列表
     * @param shopsInfoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询店铺信息列表")
    @RequestMapping(value = "/getShopsInfoListByPage",method = RequestMethod.GET)
    @RequiresPermissions("/shops-info/getShopsInfoListByPage")
    public ResponseResult<Object> getShopsInfoListByPage(@Validated ShopsInfoQueryIO shopsInfoQueryIO){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(shopsInfoQueryIO, ShopsInfoDTO.class);

        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        PageInfo<ShopsInfoDTO> pageList = shopsInfoService.getShopsInfoListByPage(shopsInfoDTO);

        List<ShopsInfoVO>  shopsInfoVOS = BeanToolsUtil.copyList(pageList.getList(),ShopsInfoVO.class);
        for (ShopsInfoVO item : shopsInfoVOS) {
            if(null != item && null != item.getHandledByPhone()){
                item.setHandledByPhone(Sm4Utils.decrypt(item.getHandledByPhone()));
            }
            if(null != item && null != item.getHandledByIdCard()){
                item.setHandledByIdCard(Sm4Utils.decrypt(item.getHandledByIdCard()));
            }

            if(null != item && item.getList().size() > 0){
                for (ShopsAttachmentVO attachmentVO: item.getList()) {
                    String relativeFileName = attachmentVO.getAttachmentPath()  + "/" +  attachmentVO.getAttachmentName() ;
                    attachmentVO.setUrlPath(urlPath+"shops/" + relativeFileName);
                    if(null != attachmentVO.getCompressFlag() && "1".equals(attachmentVO.getCompressFlag())){
                        attachmentVO.setCompressUrlPath(urlPath+"shops/" + attachmentVO.getAttachmentPath() + "/" + attachmentVO.getAttachmentName().substring(0,attachmentVO.getAttachmentName().lastIndexOf("."))+"_COMPRESS"+"."+attachmentVO.getAttachmentName().substring(attachmentVO.getAttachmentName().lastIndexOf(".")+1));
                    }
                }
            }

        }
        PageListVO<ShopsInfoVO> result = new PageListVO<>();
        result.setList(shopsInfoVOS);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据店铺ID查询店铺信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据店铺ID查询店铺信息")
    @RequestMapping(value = "/getShopsInfoById",method = RequestMethod.GET)
    @RequiresPermissions("/shops-info/getShopsInfoById")
    public ResponseResult<Object> getShopsInfoById(@RequestParam("id") Long id){
        ShopsInfoDTO shopsInfoDTO = shopsInfoService.getShopsInfoById(id);
        ShopsInfoVO shopsInfoVO = BeanToolsUtil.copyOrReturnNull(shopsInfoDTO,ShopsInfoVO.class);
        if(null != shopsInfoVO && shopsInfoVO.getHandledByPhone() != null){
            shopsInfoVO.setHandledByPhone(Sm4Utils.decrypt(shopsInfoVO.getHandledByPhone()));
        }
        if(null != shopsInfoVO && shopsInfoVO.getHandledByIdCard() != null) {
            shopsInfoVO.setHandledByIdCard(Sm4Utils.decrypt(shopsInfoVO.getHandledByIdCard()));
        }

        if(null != shopsInfoDTO && null != shopsInfoDTO.getList()){
            shopsInfoVO.setList(BeanToolsUtil.copyList(shopsInfoDTO.getList(), ShopsAttachmentVO.class));
        }

        return ResponseResult.success(shopsInfoVO);
    }

    /**
     * 新增商铺信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增商铺信息")
    @RequestMapping(value = "/saveShopsInfo",method = RequestMethod.POST)
    @RequiresPermissions("/shops-info/saveShopsInfo")
    public ResponseResult<Object> saveShopsInfo(@Validated @RequestBody ShopsInfoSaveIO bean){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        //开始身份证Base64压缩
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoFront() != null){
            shopsInfoDTO.setHandledByPhotoFrontCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoFront()));
        }
        if (null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoOpposite() != null){
            shopsInfoDTO.setHandledByPhotoOppositeCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoOpposite()));
        }

        //转换类型
        shopsInfoDTO.setLogoWidth(Double.parseDouble(bean.getLogoWidth()+""));
        shopsInfoDTO.setLogoHeight(Double.parseDouble(bean.getLogoHeight()+""));

        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(bean.getList(),ShopsAttachmentDTO.class);
        shopsInfoService.saveShopsInfo(shopsInfoDTO,list);
        return ResponseResult.success();
    }

    /**
     * 修改店铺信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "修改店铺信息")
    @RequestMapping(value = "/updateShopsInfoById",method = RequestMethod.POST)
    @RequiresPermissions("/shops-info/updateShopsInfoById")
    public ResponseResult<Object> updateShopsInfoById(@Validated @RequestBody ShopsInfoUpdateIO bean){
        ShopsInfoDTO shopsInfoDTO = BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        //开始身份证Base64压缩
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoFront() != null){
            shopsInfoDTO.setHandledByPhotoFrontCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoFront()));
        }
        if (null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoOpposite() != null){
            shopsInfoDTO.setHandledByPhotoOppositeCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoOpposite()));
        }

        //转换类型
        shopsInfoDTO.setLogoWidth(Double.parseDouble(bean.getLogoWidth()+""));
        shopsInfoDTO.setLogoHeight(Double.parseDouble(bean.getLogoHeight()+""));

        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(bean.getList(),ShopsAttachmentDTO.class);
        shopsInfoService.updateShopsInfoById(shopsInfoDTO,list);
        return ResponseResult.success();
    }

    /**
     * 删除店铺信息
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "删除店铺信息")
    @RequestMapping(value="/deleteShopsInfoById", method= RequestMethod.POST)
    @RequiresPermissions("/shops-info/deleteShopsInfoById")
    public ResponseResult<Object> deleteShopsInfoById(@Valid @RequestBody IdIO req) {
        shopsInfoService.deleteShopsInfoById(req.getId());
        return ResponseResult.success();
    }

    /**
     * 根据附件名称删除商铺附件
     * @param attachmentName
     * @param request
     * @return
     */
    @LogAnnotation(logType = "static/upload",logDesc = "根据附件名称删除商铺附件")
    @RequestMapping(value = "/deleteShopsAttachment", method = RequestMethod.POST)
    @RequiresPermissions("/shops-info/deleteShopsAttachment")
    public ResponseResult<Object> deleteShopsAttachment(@RequestParam("attachmentName") String attachmentName, HttpServletRequest request) {
        Preconditions.checkNotNull(attachmentName,"参数：attachmentName 不能为空");
        shopsInfoService.deleteAttachmentByName(attachmentName);
        return ResponseResult.success();
    }

    /**
     * 修改店铺备案状态
     * @param shopsInfoStatusIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "修改店铺备案状态")
    @RequestMapping(value = "/shops-info/updateShopsFilingsStatus",method = RequestMethod.POST)
    @RequiresPermissions("/shops-info/updateShopsFilingsStatus")
    public ResponseResult<Object> updateShopsFilingsStatus(@Validated @RequestBody ShopsInfoStatusIO shopsInfoStatusIO){
        ShopsInfoDTO bean = BeanToolsUtil.copyOrReturnNull(shopsInfoStatusIO,ShopsInfoDTO.class);
        shopsInfoService.updateShopsFilingsStatusAPI(bean);
        return ResponseResult.success();
    }

}

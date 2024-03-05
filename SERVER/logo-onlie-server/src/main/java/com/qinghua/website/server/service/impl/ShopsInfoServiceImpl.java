package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ShopsAttachmentMapper;
import com.qinghua.website.server.domain.ShopsAttachmentDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ShopsInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.server.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ShopsInfoMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 *  服务实现类
 * @since 2023-06-28
 */
@Slf4j
@Service
public class ShopsInfoServiceImpl implements ShopsInfoService {

                
    @Resource
    private ShopsInfoMapper shopsInfoMapper;

    @Resource
    private ShopsAttachmentMapper attachmentMapper;

    /**
     * 文章附件上传路径地址
     */
    @Value("${uploadPath.savePath}")
    private String savePath;

    @Override
    public List<ShopsInfoDTO> getShopsInfoList(ShopsInfoDTO bean) {
        return shopsInfoMapper.getShopsInfoList(bean);
    }

    @Override
    public PageInfo<ShopsInfoDTO> getShopsInfoListByPage(ShopsInfoDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<ShopsInfoDTO> shopsInfoList = shopsInfoMapper.getShopsInfoList(bean);
        return PageInfo.of(shopsInfoList);
    }

    @Override
    public ShopsInfoDTO getShopsInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return shopsInfoMapper.getShopsInfoById(id);
    }

    /**
     * 新增商铺信息及附件集合
     * @param bean
     * @param list
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveShopsInfo(ShopsInfoDTO bean,List<ShopsAttachmentDTO> list) {

        //根据五要素判断数据是否重复
        ShopsInfoDTO check = shopsInfoMapper.getShopsInfoByIndex(bean);
        if(null != check){
            throw new BizException("该商铺信息已存在",SysConstant.SYSTEM_ERROR_400.getCode());
        }

        shopsInfoMapper.saveShopsInfo(bean);
        List<ShopsAttachmentDTO> paramList = new ArrayList<>();

        //校验图片个数
        int yyzz = 0;//营业执照 1 张
        int zlht = 0;//租赁合同，最大3张
        int spzp = 0;//商铺照片 1 张

        if(null != list && list.size() > 0){
            for (int i = 0;i<list.size();i++){
                ShopsAttachmentDTO res = list.get(i);
                if(res.getAttachmentType().equals("1")){
                    spzp = spzp+1;
                }else if(res.getAttachmentType().equals("2")){
                    yyzz = yyzz+1;
                }else if(res.getAttachmentType().equals("3")){
                    zlht = zlht+1;
                }
            }
        }

        if(yyzz > 1){
            throw new BizException("只能上传1张营业执照照片",SysConstant.SYSTEM_ERROR_400.getCode());
        }
        if(zlht > 3){
            throw new BizException("租赁合同照片最多只能上传3张",SysConstant.SYSTEM_ERROR_400.getCode());
        }
        if(spzp > 1){
            throw new BizException("只能上传1张商铺照片",SysConstant.SYSTEM_ERROR_400.getCode());
        }

        //新增附件
        if(null != list && list.size() >0){
            for(int i=0;i<list.size();i++){
                ShopsAttachmentDTO res = list.get(i);
                res.setShopsId(bean.getId());
                paramList.add(res);
            }
            attachmentMapper.saveShopsAttachmentByList(paramList);
        }


    }

    @Override
    public void deleteShopsInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        shopsInfoMapper.deleteShopsInfoById(id);
        //删除关联附件信息，服务器数据清除
        List<ShopsAttachmentDTO> resList = attachmentMapper.getShopsAttachmentByShopsId(id);
        if(null != resList && resList.size() > 0){
            for(int i=0;i<resList.size();i++){
                //先执行服务器文件清除动作
                String filePath = savePath + "shops/" + File.separator + resList.get(i).getAttachmentPath() + "\\" + resList.get(i).getAttachmentName();
                //首先清除服务器文件
                FileUtils.deleteFile(filePath);
            }
        }
        //删除数据库附件信息
        attachmentMapper.deleteShopsAttachmentByShopsId(id);
    }

    @Override
    public void deleteShopsInfoByIdOSS(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        shopsInfoMapper.deleteShopsInfoById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateShopsInfoById(ShopsInfoDTO bean,List<ShopsAttachmentDTO> list) {
        shopsInfoMapper.updateShopsInfoById(bean);

        //校验图片个数
        int yyzz = 0;//营业执照 1 张
        int zlht = 0;//租赁合同，最大3张
        int spzp = 0;//商铺照片 1 张

        if(null != list && list.size() > 0){
            for (int i = 0;i<list.size();i++){
                ShopsAttachmentDTO res = list.get(i);
                if(res.getAttachmentType().equals("1")){
                    spzp = spzp+1;
                }else if(res.getAttachmentType().equals("2")){
                    yyzz = yyzz+1;
                }else if(res.getAttachmentType().equals("3")){
                    zlht = zlht+1;
                }
            }
        }

        if(yyzz > 1){
            throw new BizException("只能上传1张营业执照照片",SysConstant.SYSTEM_ERROR_400.getCode());
        }
        if(zlht > 3){
            throw new BizException("租赁合同照片最多只能上传3张",SysConstant.SYSTEM_ERROR_400.getCode());
        }
        if(spzp > 1){
            throw new BizException("只能上传1张商铺照片",SysConstant.SYSTEM_ERROR_400.getCode());
        }

        //附件与店铺的关系数据判断，更新接口的数据必须修改当前店铺下的附件，否则报错
        if(null != list && 0 != list.size()){
            for(ShopsAttachmentDTO att : list){
                if(bean.getId() != att.getShopsId()){
                    String type = att.getAttachmentType();
                    switch (type){
                        case "1" : throw new BizException("商铺照片所属的店铺ID不匹配",SysConstant.SYSTEM_ERROR_400.getCode());
                        case "2" : throw new BizException("营业执照所属的店铺ID不匹配",SysConstant.SYSTEM_ERROR_400.getCode());
                        case "3" : throw new BizException("租赁合同所属的店铺ID不匹配",SysConstant.SYSTEM_ERROR_400.getCode());
                        default: throw new BizException("附件所属的店铺ID不匹配",SysConstant.SYSTEM_ERROR_400.getCode());
                    }
                }
            }
        }

        //根据shopId删除表中历史数据
        attachmentMapper.deleteShopsAttachmentByShopsId(bean.getId());
        //保存新的附件信息数据
        attachmentMapper.saveShopsAttachmentByListForUpdate(list);
    }

    /**
     * 根据附件名称删除附件
     * @param attachmentName
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAttachmentByName(String attachmentName){
        //校验数据合法性
        ShopsAttachmentDTO res = attachmentMapper.getShopsAttachmentByAttachmentName(attachmentName);
        if(null != res){
            //先执行服务器文件清除动作
            String filePath = savePath + "shops/" + File.separator + res.getAttachmentPath() + "\\" + attachmentName;
            //首先清除服务器文件
            FileUtils.deleteFile(filePath);
            //执行数据库清除
            attachmentMapper.deleteAttachmentByName(attachmentName);
        }else{
            throw new BizException(SysConstant.ERROR_FILE_NOT_EXIST);
        }
    }

    /**
     * 根据附件名称删除附件
     * @param attachmentName
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAttachmentByNameOSS(String attachmentName){
        //执行数据库清除
        attachmentMapper.deleteAttachmentByName(attachmentName);
    }

    /**
     * 根据商户ID查询店铺信息
     * @param merchantId
     * @return
     */
    @Override
    public List<ShopsInfoDTO> getShopsInfoByMerchantId(Long merchantId){
        return shopsInfoMapper.getShopsInfoByMerchantId(merchantId);
    }

    /**
     * 保存店铺附件
     * @param list
     */
    @Override
    public void saveShopsAttachments(List<ShopsAttachmentDTO> list){
        list.forEach(item->{
            //逻辑代码特殊处理,每次提交文件时将原有数据剔除
            if(null != item.getAttachmentType() && "4".equals(item.getAttachmentType())){
                attachmentMapper.deleteShopsAttachmentByShopsIdAnd4Type(item.getShopsId());
            }else if(null != item.getAttachmentType() && "3".equals(item.getAttachmentType())){
                attachmentMapper.deleteShopsAttachmentByShopsIdAnd3Type(item.getShopsId());
            }else if(null != item.getAttachmentType() && "2".equals(item.getAttachmentType())){
                attachmentMapper.deleteShopsAttachmentByShopsIdAnd2Type(item.getShopsId());
            }else if(null != item.getAttachmentType() && "1".equals(item.getAttachmentType())){
                attachmentMapper.deleteShopsAttachmentByShopsIdAnd1Type(item.getShopsId());
            }
        });

        attachmentMapper.saveShopsAttachmentByList(list);
    }

    /**
     * 修改店铺备案状态
     * @param bean
     */
    @Override
    public void updateShopsFilingsStatusAPI(ShopsInfoDTO bean){
        ShopsInfoDTO res = shopsInfoMapper.getShopsInfoById(bean.getId());
        if(null != res){
            shopsInfoMapper.updateShopsFilingsStatusAPI(bean);
        }else{
            throw new BizException("没有查询到ID为"+bean.getId()+"的数据信息",SysConstant.SYSTEM_ERROR_500.getCode());
        }
    }

    /**
     * 根据商铺ID获取身份证正反面信息
     * @param shopsId
     * @return
     */
    @Override
    public ShopsInfoDTO getShopsInfoByIdAPI(Long shopsId){
        ShopsInfoDTO res = shopsInfoMapper.getShopsInfoByIdAPI(shopsId);
        return res;
    }

    /**
     * 根据ID查询身份证信息
     * @param id
     * @return
     */
    @Override
    public ShopsInfoDTO getShopsIdCardByIdAPI(Long id){
        ShopsInfoDTO res = shopsInfoMapper.getShopsIdCardByIdAPI(id);
        return res;
    }

}

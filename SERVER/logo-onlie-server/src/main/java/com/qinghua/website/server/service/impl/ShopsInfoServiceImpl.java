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
@Service
public class ShopsInfoServiceImpl implements ShopsInfoService {

                
    @Resource
    private ShopsInfoMapper shopsInfoMapper;

    @Resource
    private ShopsAttachmentMapper attachmentMapper;

    /**
     * 文章附件上传路径地址
     */
    @Value("${upload.path.shops}")
    private String shopsPath;

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

        shopsInfoMapper.saveShopsInfo(bean);
        List<ShopsAttachmentDTO> paramList = new ArrayList<>();
        //新增附件
        if(null != list && list.size() >0){
            for(int i=0;i<list.size();i++){
                ShopsAttachmentDTO res = list.get(i);
                res.setShopsId(bean.getId());
                paramList.add(res);
            }
        }
        attachmentMapper.saveShopsAttachmentByList(paramList);
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
                String filePath = shopsPath + File.separator + resList.get(i).getAttachmentPath() + "\\" + resList.get(i).getAttachmentName();
                //首先清除服务器文件
                FileUtils.deleteFile(filePath);
            }
        }
        //删除数据库附件信息
        attachmentMapper.deleteShopsAttachmentByShopsId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateShopsInfoById(ShopsInfoDTO bean,List<ShopsAttachmentDTO> list) {
        shopsInfoMapper.updateShopsInfoById(bean);
        //更新附件信息，清除表中附件数据
        if(null != list && list.size() > 0){
            for (int i = 0; i<list.size(); i++){
                attachmentMapper.deleteAttachmentByName(list.get(i).getAttachmentName());
            }
            //保存新的附件信息数据
            attachmentMapper.saveShopsAttachmentByList(list);
        }
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
            String filePath = shopsPath + File.separator + res.getAttachmentPath() + "\\" + attachmentName;
            //首先清除服务器文件
            FileUtils.deleteFile(filePath);
            //执行数据库清除
            attachmentMapper.deleteAttachmentByName(attachmentName);
        }else{
            throw new BizException(SysConstant.ERROR_FILE_NOT_EXIST);
        }
    }
}

package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ChannelMapper;
import com.qinghua.website.server.dao.ContentMapper;
import com.qinghua.website.server.domain.ChannelDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ContentMapper contentMapper;

    /**
     * 获取单个对象
     * @param id    主键
     * @return 结果对象
     */
    public ChannelDTO getChannelByID(Long id){
        return channelMapper.getChannelByID(id);
    }

    /**
     * 查询对象列表
     * @param bean  查询条件对象
     * @return  分页对象
     */
    public PageInfo<ChannelDTO> getChannelListByPage(ChannelDTO bean){
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<ChannelDTO> channelDTOList = channelMapper.getChannelListByPage(bean);
        return PageInfo.of(channelDTOList);
    }

    /**
     * 保存单个对象
     * @param bean  保存对象
     * @return 主键
     */
    public void saveChannel(ChannelDTO bean){
        //校验栏目是否已经存在，重名则报错 TODO
        channelMapper.saveChannel(bean);
    }

    /**
     * 更新单个对象 id必须有
     * @param bean  更新对象
     * @return 更新的记录条数
     */
    public void updateChannel(ChannelDTO bean){
        channelMapper.updateChannel(bean);
    }

    /**
     * 按主键删除对象
     * @param id    主键
     * @return 删除的记录条数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteChannelByID(Long id){
        //如果有子栏目 不允许删除，如果栏目下有文字不允许删除
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setParentId(id);

        List<ChannelDTO> subChannelCheckList = channelMapper.getChannelByParentID(channelDTO);
        //存在子栏目，不允许删除
        if(null != subChannelCheckList && subChannelCheckList.size() > 0){
            throw new BizException(SysConstant.ERROR_CHANNEL_HAVE_SUB);
        }

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setChannelId(id);
        List<ContentDTO> contentDTOList = contentMapper.getContentByChannelId(contentDTO);
        //栏目下存在文章不允许删除
        if(null != contentDTOList && contentDTOList.size() > 0){
            throw new BizException(SysConstant.ERROR_CHANNEL_HAVE_CONTENT);
        }

        channelMapper.deleteChannelByID(id);

    }

}

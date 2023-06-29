package com.qinghua.website.server.dao;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.ChannelDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChannelMapper {

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public ChannelDTO getChannelByID(Long id);

    /**
     * 查询对象列表
     * @param bean  查询条件对象
     * @return  分页对象
     */
    public List<ChannelDTO> getChannelListByPage(ChannelDTO bean);

    /**
     * 保存单个对象
     * @param bean  保存对象
     * @return 主键
     */
    public void saveChannel(ChannelDTO bean);

    /**
     * 更新单个对象 id必须有
     * @param bean  更新对象
     * @return 更新的记录条数
     */
    public void updateChannel(ChannelDTO bean);

    /**
     * 按主键删除对象
     * @param id    主键
     * @return 删除的记录条数
     */
    public void deleteChannelByID(Long id);

    /**
     * 根据parentID查询子栏目集合
     * @param channelDTO
     * @return
     */
    public List<ChannelDTO> getChannelByParentID(ChannelDTO channelDTO);


}

package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.ChannelDTO;

public interface ChannelService {

    /**
     * 获取单个对象
     * @param id    主键
     * @return 结果对象
     */
    public ChannelDTO getChannelByID(Long id);

    /**
     * 查询对象列表
     * @param bean  查询条件对象
     * @return  分页对象
     */
    public PageInfo<ChannelDTO> getChannelListByPage(ChannelDTO bean);

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

}
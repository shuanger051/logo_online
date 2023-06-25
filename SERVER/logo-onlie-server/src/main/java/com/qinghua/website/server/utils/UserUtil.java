package com.qinghua.website.server.utils;

import com.qinghua.website.server.domain.SysUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author yzhang
 * @date 2020/11/3
 * @desc 获取登录用户的相关信息
 */
@Slf4j
public class UserUtil {

    private static final ThreadLocal<SysUserDTO> local = new ThreadLocal<>();

    /**
     * 获取系统用户信息，放入到本地线程中，在程序中要获取的话可以直接调用
     * @return
     */
    public static SysUserDTO getUser() {
        SysUserDTO user = local.get();
        if (user == null) {
            try {
                Subject subject = SecurityUtils.getSubject();
                //获取session
                Session session = subject.getSession();
                //把用户信息保存到session
                user = (SysUserDTO) session.getAttribute("user");
                local.set(user);
            } catch (Exception ex) {
                log.debug("获取不到用户信息...{}", ex.getMessage());
            }
        }
        return user;
    }

    /**
     * 创建虚拟用户，只给系统内部使用
     */
    public static void setVirtualUser() {
        SysUserDTO dto = local.get();
        if (dto == null) {
            SysUserDTO user = new SysUserDTO();
            user.setId(9999L);
            local.set(user);
        }
    }

    /**
     * 移除线程中的用户信息
     */
    public static void removeUser() {
        local.remove();
    }

}

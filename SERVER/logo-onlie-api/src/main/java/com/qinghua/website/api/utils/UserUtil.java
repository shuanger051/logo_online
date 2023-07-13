package com.qinghua.website.api.utils;

import com.qinghua.website.api.common.SessionUser;
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

    private static final ThreadLocal<SessionUser> local = new ThreadLocal<>();

    /**
     * 获取系统用户信息，放入到本地线程中，在程序中要获取的话可以直接调用
     *
     * @return
     */
    public static SessionUser getUser() {
        SessionUser user = local.get();
        if (user == null) {
            try {
                Subject subject = SecurityUtils.getSubject();
                //获取session
                Session session = subject.getSession();
                //把用户信息保存到session
                user = (SessionUser) session.getAttribute(SessionUser.SEESION_USER);
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
        SessionUser user = local.get();
        if (user == null) {
            SessionUser vuser = new SessionUser();
            vuser.setId(9999L);
            local.set(vuser);
        }
    }

    /**
     * 移除线程中的用户信息
     */
    public static void removeUser() {
        local.remove();
    }

}

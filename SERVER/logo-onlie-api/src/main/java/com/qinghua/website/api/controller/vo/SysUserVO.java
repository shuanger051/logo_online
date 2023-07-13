package com.qinghua.website.api.controller.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserVO {

    /**
     * id:
     */
    private Long id;

    /**
     * user_name:用户名
     */
    @Excel(name = "用户名", width = 20, needMerge = true)
    private String userName;

    /**
     * email:电子邮箱
     */
    @Excel(name = "电子邮箱", width = 30, needMerge = true)
    private String email;

    /**
     * register_time:注册时间
     */
    @Excel(name = "注册时间", width = 30, format = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;

    /**
     * register_ip:注册IP
     */
    @Excel(name = "注册IP", width = 30, needMerge = true)
    private String registerIp;

    /**
     * last_login_time:最后登录时间
     */
    private Date lastLoginTime;

    /**
     * last_login_ip:最后登录IP
     */
    private String lastLoginIp;

    /**
     * login_count:
     */
    private Integer loginCount;

    /**
     * error_time:登录错误时间
     */
    private Date errorTime;

    /**
     * error_count:登录错误次数
     */
    private Integer errorCount;

    /**
     * error_ip:登录错误IP
     */
    private String errorIp;

    /**
     * reset_key:重置密码KEY
     */
    private String resetKey;

    /**
     * reset_pwd:重置密码VALUE
     */
    private String resetPwd;

    /**
     * activation:激活状态
     */
    @Excel(name = "激活状态", width = 20, replace = {"未激活_0", "激活_1"})
    private String activation;

    /**
     * activation_code:激活码
     */
    private String activationCode;

    /**
     * is_admin:是否管理员 0 不是 1 是
     */
    @Excel(name = "是否超管", width = 20, replace = {"不是_0", "是_1"})
    private String isAdmin;

    /**
     * is_disabled:0 不禁用 1 禁用
     */
    @Excel(name = "是否禁用", width = 20, replace = {"不禁用_0", "禁用_1"})
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserVO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", registerTime=" + registerTime +
                ", registerIp='" + registerIp + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", loginCount=" + loginCount +
                ", errorTime=" + errorTime +
                ", errorCount=" + errorCount +
                ", errorIp='" + errorIp + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", resetPwd='" + resetPwd + '\'' +
                ", activation=" + activation +
                ", activationCode='" + activationCode + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }

}

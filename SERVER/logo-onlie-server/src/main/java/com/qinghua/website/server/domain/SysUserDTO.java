package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserDTO extends BaseDTO {

    /**
     * id:
     */
    private Long id;
    
    /**
     * user_name:用户名
     */
    private String userName;
    
    /**
     * email:电子邮箱
     */
    private String email;
    
    /**
     * password:密码
     */
    private String password;
    
    /**
     * register_time:注册时间
     */
    private Date registerTime;
    
    /**
     * register_ip:注册IP
     */
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
    private String activation;
    
    /**
     * activation_code:激活码
     */
    private String activationCode;
    
    /**
     * is_admin:是否管理员 0 不是 1 是
     */
    private String isAdmin;
    
    /**
     * is_disabled:0 不禁用 1 禁用
     */
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
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

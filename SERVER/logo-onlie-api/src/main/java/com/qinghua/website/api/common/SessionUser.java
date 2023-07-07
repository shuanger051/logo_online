package com.qinghua.website.api.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionUser implements Serializable {

    private static final long serialVersionUID = -1045486377820200117L;

    public static final String SEESION_USER = "user";

    public static final String USER_IMG_CODE =  "user_img_code";

    public static final String IS_ADMIN_VALUE = "1";

    public static final String USER_LOCK_STATUS_IS_DISABLED = "1";


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
     * activation:激活状态
     */
    private Boolean activation;

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
        return "SessionUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", activation=" + activation +
                ", isAdmin='" + isAdmin + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }

}

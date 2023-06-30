package com.qinghua.website.server.constant;

public enum SysConstant implements IEnum {

    SUCCESS("0", "操作成功"),
    TOKEN_ERROR("1", "TOKEN异常"),
    REDIS_ERROR_2("2", "Redis操作失败"),
    TOKEN_EXPIRE_ERROR("3", "TOKEN超期"),
    SYSTEM_ERROR_400("400", "请求参数错误"),
    SYSTEM_ERROR_401("401", "凭据异常,拒绝访问"),
    SYSTEM_ERROR_403("403", "权限校验失败"),
    SYSTEM_ERROR_404("404", "资源不存在"),
    SYSTEM_ERROR_405("405", "不支持的HTTP RequestMethod"),
    SYSTEM_ERROR_500("500", "服务器内部错误"),
    RSA_ERROR_10001("10001","密碼解析錯誤!"),
    LOGIN_ERROR_10002("10002","账户已锁定,请联系管理员处理!"),
    RSA_ERROR_10003("10003","获取加密key错误,请重试!"),
    ERROR_FILE_UPLOAD_FILE_10004("10004", "文件上传异常异常"),
    ERROR_DICT_DELETE_DICT_10005("10005", "数据字典删除失败"),
    ERROR_DICT_CHECK_DICTNAME_10006("10006", "数据字典名称已存在"),
    ERROR_DICT_CHECK_DICTKEY_10007("10007", "数据字典编码已存在"),
    ERROR_DICT_CHECK_DICTKEY_10008("10008", "数据字典编码不存在"),
    ERROR_DICT_CHECK_ITEMKEY_10009("10009", "数据字典子项编码已存在"),
    ERROR_DICT_CHECK_ITEMVALUE_10010("10010", "数据字典子项名称已存在"),
    ERROR_CONFIG_CHECK_CONFIGNO_10011("10011", "系统配置KEY已存在"),
    ERROR_CONFIG_CHECK_CONFIGNAME_10012("10012", "系统配置名称已存在"),
    ERROR_CONFIG_GET_VALUE_WRONG_10013("10013", "获取系统配置信息错误"),
    ERROR_WRONG_LIST_PARAMS_10014("10014", "存在重复数据"),
    ERROR_ENCODE_RSA_WRONG_10015("10015","签名加密失败"),
    ERROR_DECODE_RSA_WRONG_10016("10016","签名解密失败"),
    ERROR_NOLY_SUPER_ADMIN_CAN_DO("10017","只有超级管理员才可以对此数据操作"),
    ERROR_MUST_CREATE_IMG_CODE_FIRST("10018","请先点击页面按钮生成图片验证码"),
    ERROR_CHANNEL_HAVE_CONTENT("10019","该栏目下已有文章，请先删除文章。"),
    ERROR_CUSTCODE_ORGNAME_PARENTID_EXIT_10020("10020","已存在相同级别同名机构"),
    ERROR_SYS_USER_NOT_EXISTS("10021","用户管理员不存在"),
    ERROR_USER_NOT_LOGIN("10022","用户未登录不允许操作"),
    ERROR_SECURITY_ANSWER_ILLEGAL_INCOMING_DATA_10023("10023","传入参数ID非法"),
    ERROR_SYS_CONFIG_KEY_IS_NOT_EXIST("10024","系统配置参数不存在，请检查初始化配置"),
    ERROR_USER_NOT_SAME_CHANNEL_100029("10025","无此用户ID"),
    ERROR_CHANNEL_HAVE_SUB("10026","存在子栏目，请先删除子栏目"),
    ERROR_MERCHANT_IS_EXISTS("10027","当前身份证号信息的商户已存在"),
    ERROR_SECURITY_ANSWER_PERMISSION_EXCEPTION_10028("10028","权限异常"),
    ERROR_GET_RSA_PRIVATEKEY_WRONG_10029("10029","获取RSA密钥失败"),
    ERROR_GET_RSA_PUBLICKEY_WRONG_10030("10030","获取RSA公钥失败"),
    ERROR_BIND_USER("10031", "身份验证失败"),
    ERROR_USER_CHECK_OLD_PWD("10032", "旧密码错误"),
    ERROR_USER_UNKNOWN_ACCOUNT("10033", "未知账户"),
    ERROR_USER_WRONG_PWD("10034", "密码不正确"),
    ERROR_USER_LOCKED_ACCOUNT("10035", "账户已锁定"),
    ERROR_USER_EXCESSIVE_ATTEMPTS("10036", "用户名或密码错误次数过多"),
    ERROR_USER_WRONG_PWD_OR_USERNAME("10037", "用户名或密码不正确"),
    ERROR_USER_UN_LOGIN("10038", "未登录或者登录已失效"),
    ERROR_USER_NO_RIGHTS("10039", "无权限"),
    ERROR_USER_REPEAT_USERNAME("10040","用户名已存在"),
    ERROR_IMG_CODE_IS_WRONG("10041","图片验证码错误"),
    ERROR_HAVE_SHOPSINFO("10042","当前商户名下有归属的店铺信息，不允许删除"),
    ERROR_USER_CHECK_NEW_PWD_NOTNULL("10043", "新密码不能为空"),
    ERROR_USER_CHECK_OLD_PWD_NOTNULL("10044", "旧密码不能为空"),
    ERROR_USER_CHECK_OLDNEW_PWD_NOT_EQUALS("10045", "新旧密码不能相同"),
    ERROR_USER_REPEAT_ROLENAME("10046","角色名称已存在"),
    ERROR_USER_REPEAT_PERMSNAME("10047","权限名称已存在"),
    ERROR_USER_REPEAT_PERMSPATH("10048","权限路径已存在"),
    ERROR_USER_REPEAT_USERROLE("10049","用户角色信息已存在"),
    ERROR_USER_REPEAT_ROLEPERMS("10050","角色权限信息已存在"),
    ERROR_USER_HEADIMG_FORMAT_NOT_SUPPORT("10051","请选择.png.jpg格式的图片"),
    ERROR_USER_HEADIMG_SIZE_TO0LARGE("10052","头像大小不能超过30KB"),
    ERROR_USER_PHONE_ISNULL("10053","手机号非法"),
    ERROR_LOGIN_CHECK_FAILED("10054","请刷新页面重新尝试"),
    ERROR_USER_NOT_HAVE_ROLE("10055","未查询到此角色信息"),
    ERROR_USER_NOT_HAVE_ROLE_2("10056","批量参数中存在不属于当前用户所属机构的角色"),
    ERROR_USER_NOT_HAVE_ROLE_3("10057","批量参数中存在不属于当前用户所属机构的用户"),
    ERROR_USER_NOT_HAVE_ROLE_4("10058","无此角色信息"),
    ERROR_USER_ONLY_CHANGE_YOURSELF_PASSWORD("10059","用户仅限修改本人密码"),
    ERROR_USER_NOT_SAME_CHANNEL("10060","无此用户信息"),
    ERROR_USER_REPEAT_PERMSNAME_UNDEFIND("10061","该权限不存在"),
    ERROR_CHECK_START_DATE("10062", "开始日期格式不正确"),
    ERROR_CHECK_END_DATE("10063", "结束日期格式不正确"),
    ERROR_CHECK_START_TIME("10064", "开始时间格式不正确"),
    ERROR_CHECK_END_TIME("10065", "结束时间格式不正确"),
    ERROR_CHECK_DATE_INTERVAL("10066", "开始日期不能晚于结束日期"),
    ERROR_DATE_FORMAT("10067","时间格式化出错"),
    ERROR_LOGIN_WRONG_MANY_TIMES("10068","错误次数过多");

    private final String code;

    private final String msg;

    private SysConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}


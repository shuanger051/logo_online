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
    ERROR_UNKNOWN_SIGN_WRONG_10017("10017","签名证书非法，系统已拒绝访问"),
    ERROR_ANSWER_NUM_LACK_10018("10018","必须且只能绑定三个问题"),
    ERROR_QUESTION_EXIT_10019("10019","用户问题不可以重复"),
    ERROR_CUSTCODE_ORGNAME_PARENTID_EXIT_10020("10020","已存在相同级别同名机构"),
    ERROR_SYS_USER_NOT_EXISTS_10021("10021","用户管理员不存在"),
    ERROR_SECURITY_ANSWER_NOTHING_SECURITY_PROBLEM_10022("10022","该用户还未设置密保问题"),
    ERROR_SECURITY_ANSWER_ILLEGAL_INCOMING_DATA_10023("10023","传入参数ID非法"),
    ERROR_SECURITY_QUESTION_NOT_FOUND_QUESTIONNAME_10024("10024","密保问题不存在"),
    ERROR_USER_NOT_SAME_CHANNEL_100029("10025","当前用户所属机构下无此用户ID"),
    ERROR_SECURITY_QUESTION_EXTIST_USER_BIND_10026("10026","该问题已有用户绑定"),
    ERROR_USER_NOT_IN_PROJECT_10027("10027","当前用户与绑定的证书非同一渠道，请重新绑定证书"),
    ERROR_SECURITY_ANSWER_PERMISSION_EXCEPTION_10028("10028","权限异常"),
    ERROR_GET_RSA_PRIVATEKEY_WRONG_10029("10029","获取RSA密钥失败"),
    ERROR_GET_RSA_PUBLICKEY_WRONG_10030("10030","获取RSA公钥失败");

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


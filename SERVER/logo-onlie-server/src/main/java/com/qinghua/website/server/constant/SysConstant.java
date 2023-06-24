package com.qinghua.website.server.constant;

public enum SysConstant implements IEnum {

    SUCCESS(0, "操作成功"),
    TOKEN_ERROR(1, "TOKEN异常"),
    REDIS_ERROR_2(2, "Redis操作失败"),
    TOKEN_EXPIRE_ERROR(3, "TOKEN超期"),
    SYSTEM_ERROR_400(400, "请求参数错误"),
    SYSTEM_ERROR_401(401, "凭据异常,拒绝访问"),
    SYSTEM_ERROR_403(403, "权限校验失败"),
    SYSTEM_ERROR_404(404, "资源不存在"),
    SYSTEM_ERROR_405(405, "不支持的HTTP RequestMethod"),
    SYSTEM_ERROR_500(500, "服务器内部错误"),
    RSA_ERROR_10001(10001,"密碼解析錯誤!"),
    LOGIN_ERROR_10002(10002,"账户已锁定,请联系管理员处理!"),
    RSA_ERROR_10003(10003,"获取加密key错误,请重试!");

    private final int code;

    private final String msg;

    private SysConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}


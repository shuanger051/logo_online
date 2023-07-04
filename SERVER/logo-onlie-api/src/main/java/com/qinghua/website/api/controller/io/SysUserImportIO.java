package com.qinghua.website.api.controller.io;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.qinghua.website.api.validation.MultipleLayerValidator;
import com.qinghua.website.api.validation.MyEmail;
import lombok.Data;

@Data
public class SysUserImportIO  implements IExcelDataModel, IExcelModel {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * user_name:用户名
     */
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "用户名称不能为空",
            max = 100, lengthMessage = "用户名称不能超过100个字符")
    @Excel(name = "用户名")
    private String userName;

    /**
     * email:电子邮箱
     */
    @MyEmail(message = "邮箱格式不正确")
    @Excel(name = "电子邮箱")
    private String email;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}

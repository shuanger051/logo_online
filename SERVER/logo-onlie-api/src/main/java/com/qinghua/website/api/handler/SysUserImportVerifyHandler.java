package com.qinghua.website.api.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.qinghua.website.api.controller.io.SysUserImportIO;
import com.qinghua.website.server.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class SysUserImportVerifyHandler implements IExcelVerifyHandler<SysUserImportIO> {

    private final ThreadLocal<List<SysUserImportIO>> threadLocal = new ThreadLocal<>();

    @Resource
    private SysUserService sysUserService;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(SysUserImportIO inputEntity) {
        StringJoiner joiner = new StringJoiner(",");
        // 根据姓名判断数据是否重复
        String userName = inputEntity.getUserName();
        boolean duplicates = sysUserService.checkSysUserIsExist(userName);
        if (duplicates) {
            joiner.add("数据与数据库数据重复");
        }

        List<SysUserImportIO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.equals(inputEntity)) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("数据与第" + lineNumber + "行重复");
            }
        });
        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(inputEntity);
        threadLocal.set(threadLocalVal);
        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<SysUserImportIO>> getThreadLocal() {
        return threadLocal;
    }

}

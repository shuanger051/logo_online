package com.qinghua.website.api.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.SysUserImportIO;
import com.qinghua.website.api.controller.io.SysUserQueryIO;
import com.qinghua.website.api.controller.vo.SysUserVO;
import com.qinghua.website.api.handler.SysUserImportVerifyHandler;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/easyPoi")
public class EasyPoiController {

    @Autowired
    private SysUserService sysUserService;

    @Resource
    private SysUserImportVerifyHandler sysUserImportVerifyHandler;

    /**
     * 导出系统用户列表
     * @param sysUserQueryIO
     * @param map
     * @param request
     * @param response
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "导出系统用户列表")
    @RequestMapping(value = "/exportExcelByUserListPage", method = RequestMethod.GET)
    @RequiresPermissions("/easyPoi/exportExcelByUserListPage")
    public ResponseResult<Object> exportExcelByUserListPage(@Validated SysUserQueryIO sysUserQueryIO,
                                          ModelMap map, HttpServletRequest request,
                                          HttpServletResponse response){

        SysUserDTO queryDTO = BeanToolsUtil.copyOrReturnNull(sysUserQueryIO,SysUserDTO.class);
        PageInfo<SysUserDTO> pageList =  sysUserService.getSysUserList(queryDTO);
        List<SysUserVO> sysUserVOList =  BeanToolsUtil.copyAsList(pageList.getList(),SysUserVO.class);

        ExportParams params = new ExportParams("系统用户列表", "系统用户列表", ExcelType.XSSF);
        map.put(NormalExcelConstants.DATA_LIST, sysUserVOList);
        map.put(NormalExcelConstants.CLASS, SysUserVO.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "系统用户列表");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

        return ResponseResult.success("导出成功！");
    }

    /**
     * 导入系统用户表格
     * @param file
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "导入系统用户列表")
    @RequestMapping(value = "/importExcelByUserList", method = RequestMethod.POST)
    @RequiresPermissions("/easyPoi/importExcelByUserList")
    public ResponseResult<Object> importExcelByUserList(@RequestPart("file")MultipartFile file){

        checkExcelFile(file);

        //设置表头
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        //设置验证支持 验证支持开开之后才会判断实体类中的限制
        params.setNeedVerify(true);
        params.setVerifyHandler(sysUserImportVerifyHandler);
        ExcelImportResult<SysUserImportIO> result;
        List<SysUserImportIO> failList = new ArrayList<>();

        try {
            result = ExcelImportUtil.importExcelMore(file.getInputStream(), SysUserImportIO .class, params);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (result != null) {
            if (result.isVerifyFail()) {
                failList = result.getFailList().stream().filter(b -> !BeanUtil.isEmpty(b, "errorMsg", "rowNum")).collect(Collectors.toList());
            }
            if (Integer.sum(result.getList().size(), failList.size()) == 0) {
                throw new IllegalArgumentException("该文件无数据");
            }
        }

        if(null != failList && failList.size() > 0){
            return ResponseResult.error(failList.toString());
        }else{
            List<SysUserDTO> list = BeanToolsUtil.copyList(result.getList(),SysUserDTO.class);
            sysUserService.saveSysUserByList(list);
            return ResponseResult.success("导入成功！");
        }

    }

    /**
     * 下载Excel导入模板
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "下载Excel导入模板")
    @RequestMapping(value = "/downloadExcelTemplate", method = RequestMethod.GET)
    @RequiresPermissions("/easyPoi/downloadExcelTemplate")
    public void downloadExcelTemplate(HttpServletRequest req, HttpServletResponse res,String fileName) throws IOException {

        Preconditions.checkNotNull(fileName, "参数:模板名称不能为空！");
        FileInputStream input = null;
        OutputStream out = null;
        try {
            //设置要下载的文件的名称
            res.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //文件的MIME类型
            res.setContentType("application/octet-stream;charset=UTF-8");
            //获取文件的路径
            String filePath = getClass().getResource("/excel_template/" + fileName).getPath();
            input = new FileInputStream(filePath);
            out = res.getOutputStream();

            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
            res.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();

        } catch (Exception ex) {
            log.error("Excel模板下载出错!");
        }finally {
            if (input != null) {
                input.close();
                input = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        }

    }

    /**
     * 校验Excel导入格式是否合法
     * @param file
     * @return
     */
    public void checkExcelFile(MultipartFile file){
        if (!file.isEmpty()) {
            //文件名称
            int begin = Objects.requireNonNull(file.getOriginalFilename()).indexOf(".");
            //文件名称长度
            int last = file.getOriginalFilename().length();
            if (file.getSize() > 5242880) {
                throw new IllegalArgumentException("上传文件不可超5M");
            }
            //判断文件格式是否正确
            String fileName = file.getOriginalFilename().substring(begin, last);
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
                throw new IllegalArgumentException("上传文件格式有误,仅支持xls、xlsx文件");
            }
        } else {
            throw new IllegalArgumentException("该文件无数据");
        }
    }

}

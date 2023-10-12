package com.qinghua.website.api.controller;

import com.qinghua.website.api.LogoApplication;
import com.qinghua.website.server.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/application")
public class ApplicationController {

    @GetMapping("/restart")
    public ResponseResult<Object> restart(){
        log.info("开始重启服务……");
        LogoApplication.restart();
        return ResponseResult.success();
    }

}

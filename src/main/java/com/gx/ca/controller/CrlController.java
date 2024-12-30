package com.gx.ca.controller;

import com.gx.ca.mapper.Crl;
import com.gx.ca.service.CrlService;
import com.gx.ca.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("crl")
public class CrlController {
    @Resource
    CrlService crlService;
    @PostMapping("list")
    public Result listCrl(){
        return crlService.listCrl();
    }
}

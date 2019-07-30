package com.example.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.demo.callback.R;
import com.yuepai.yuepaiserver.entity.po.Test;
import com.yuepai.yuepaiserver.service.test.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/yp/admin")
public class TestController {
    @Reference(version = "1.0.0",check = true)
    private TestService testService;
    @GetMapping(value = "/getTest")
    public R getAllTest(){
        List<Test> list = new ArrayList<Test>();
        list = testService.getAll();
        return new R(true,R.CODE_SUCCESS,list,"");
    }
    public void testMerg(){

    }
}

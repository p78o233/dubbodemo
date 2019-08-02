package com.example.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.callback.R;
import com.yuepai.yuepaiserver.entity.po.Test;
import com.yuepai.yuepaiserver.service.redis.RedisTemplateService;
import com.yuepai.yuepaiserver.service.spi.SpiService;
import com.yuepai.yuepaiserver.service.test.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/yp/admin")
public class TestController {
    @Reference(version = "1.0.0",check = true)
    private TestService testService;
    @Reference(version = "1.0.0",check = true)
    private RedisTemplateService rts;
//    spi测试service
    @Reference(group = "group1",version = "1.0.0",check = true)
    private SpiService spiServiceFirst;
    @Reference(group = "group2",version = "1.0.0",check = true)
    private SpiService spiServiceSecond;
//    测试接口
    @GetMapping(value = "/getTest")
    public R getAllTest(){
        List<Test> list = new ArrayList<Test>();
        list = testService.getAll();
        return new R(true,R.CODE_SUCCESS,list,"");
    }
//    redis测试接口们
    @GetMapping(value = "/setRedis")
    public void setRedis(HttpServletRequest request){
        String name = request.getParameter("name");
        rts.set("name",name);
    }
    @GetMapping(value = "/getRedis/{key}")
    public R getRedis(@PathVariable("key")String key){
        return new R (true,R.CODE_SUCCESS,rts.get(key),"");
    }

//    spi测试接口
    @GetMapping(value = "/spiFirst")
    public JSONObject spiFirst(){
        return spiServiceFirst.testSpi();
    }
    @GetMapping(value = "/spiSecond")
    public JSONObject spiSecond(){
        return spiServiceSecond.testSpi();
    }
}

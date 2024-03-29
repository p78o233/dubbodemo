package com.example.demo.controller;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.callback.R;
import com.yuepai.yuepaiserver.entity.po.Test;
import com.yuepai.yuepaiserver.service.redis.RedisTemplateService;
import com.yuepai.yuepaiserver.service.spi.SpiApolloService;
import com.yuepai.yuepaiserver.service.spi.SpiService;
import com.yuepai.yuepaiserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/yp/admin")
public class TestController {
    @Value("${dubbo.spi.group}")
//    通过apollo动态的组名传入
    public String groupName;
//    引用远程服务
    private static ReferenceConfig<SpiApolloService> referenceConfig= null;
//    通讯细节
    private static ReferenceConfigCache cache = null;
//    上一次使用的组别名
    public static String initGroupName = "";

    @Reference(version = "1.0.0",check = true)
    private TestService testService;
    @Reference(version = "1.0.0",check = true)
    private RedisTemplateService rts;
//    测试apollo动态转换接口
    private SpiApolloService spiApolloService;
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
//    apollo动态接口测试
    @GetMapping(value = "/spiRunOrDev")
    public JSONObject spiRunOrDev(){
        if("".equals(initGroupName)){
            initGroupName = groupName;
            spiApolloService = getInvokeService(groupName,"1.0.0",false);
        }else {
            if(initGroupName.equals(groupName)){
                initGroupName = groupName;
                spiApolloService = getInvokeService(groupName,"1.0.0",true);
            }else{
                initGroupName = groupName;
                spiApolloService = getInvokeService(groupName,"1.0.0",false);
            }
        }

       return spiApolloService.showNowEvn();
    }
    public SpiApolloService getInvokeService(String groupName,String version,boolean flag) {
//        flag == false的时候创建新对象
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("yuepai-client");
        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("127.0.0.1:2181");
        registry.setProtocol("zookeeper");
        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
        // 引用远程服务
        // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        if(referenceConfig == null||!flag) {
            referenceConfig = new ReferenceConfig<>();
        }
        referenceConfig.setApplication(application);
        // 多个注册中心可以用setRegistries()
        referenceConfig.setRegistry(registry);
        referenceConfig.setGroup(groupName);
        referenceConfig.setVersion(version);
        referenceConfig.setInterface(SpiApolloService.class);
        //         和本地bean一样使用xxxService
        // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
        if(cache == null||!flag) {
            cache = ReferenceConfigCache.getCache();
        }
        return cache.get(referenceConfig);
    }

    private SpiApolloService getInvokeService(String groupName) {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("test_xzh");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setProtocol("dubbo");
        registry.setPort(20880);
        registry.setTimeout(60000);

        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
        // 引用远程服务
        // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        ReferenceConfig<SpiApolloService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        // 多个注册中心可以用setRegistries()
        reference.setRegistry(registry);
        reference.setInterface(SpiApolloService.class);
        reference.setVersion("1.0.0");
        reference.setGroup(groupName);

        // 和本地bean一样使用xxxService
        // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        return cache.get(reference);
    }
//    短信模板测试
    @PostMapping(value = "/testMessage")
    public void testMessage(@RequestBody JSONObject jsonObject){
        String str = "你好 {name},订单号{orderNun}";
//        获取appId
        String appId =  jsonObject.getString("appId");
//        获取需要发送短信的list
        String listStr = jsonObject.getString("list");
        JSONArray jsonArray = JSONArray.parseArray(listStr);

        ArrayList<String>  words=new ArrayList<String>();
        int m=0,n=0;
        int count=0;
//        获取短信模板中的key
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='{'){
                if(count==0){
                    m=i;
                }
                count++;
            }
            if(str.charAt(i)=='}'){
                count--;
                if(count==0){
                    n=i;
//                    words.add(str.substring(m,n+1));
                    words.add(str.substring(m+1,n));
                }
            }
        }
//        短信数组填写内容
        for(int i=0;i<jsonArray.size();i++){
            String strReplace = str;
            for(String word : words){
//                strReplace = strReplace.replace(word,JSONObject.parse(jsonArray.getString);
            }
            strReplace = strReplace.replaceAll("[\\{\\}]","");

        }


        System.out.println(str);
    }
}

package com.yuepai.yuepaiserver.service.spi.impl;/*
 * @author p78o2
 * @date 2019/8/2
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.yuepai.yuepaiserver.service.spi.SpiService;

@Service(group = "group1",version = "1.0.0",timeout = 10000)
public class SpiServiceFirst implements SpiService{
    @Override
    public JSONObject testSpi() {
        JSONObject jo = new JSONObject();
        jo.put("data","第一个实现类");
        return jo;
    }
}

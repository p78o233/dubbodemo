package com.yuepai.yuepaiserver.service.spi.impl;/*
 * @author p78o2
 * @date 2019/8/5
 */

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.yuepai.yuepaiserver.service.spi.SpiApolloService;

@Service(group = "dev", version = "1.0.0", timeout = 10000)
public class SpiApolloServiceImplDev implements SpiApolloService{
    @Override
    public JSONObject showNowEvn() {
        String str = "dev";
        JSONObject jo = new JSONObject();
        jo.put("evn", str);
        return jo;
    }

}

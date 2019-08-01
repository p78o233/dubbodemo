package com.yuepai.yuepaiserver.service.redis.impl;/*
 * @author p78o2
 * @date 2019/7/31
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.yuepai.yuepaiserver.service.redis.RedisTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service(version = "1.0.0",timeout = 10000,loadbalance ="roundrobin")
public class RedisTemplateServiceImpl implements RedisTemplateService{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public <T> boolean set(String key, T value) {
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object get(String key) {
      return key == null ?null : redisTemplate.opsForValue().get(key);
    }

}

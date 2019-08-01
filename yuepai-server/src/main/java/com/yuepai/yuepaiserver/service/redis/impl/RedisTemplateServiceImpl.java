package com.yuepai.yuepaiserver.service.redis.impl;/*
 * @author p78o2
 * @date 2019/7/31
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.yuepai.yuepaiserver.service.redis.RedisTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

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

    @Override
    @SuppressWarnings("unchecked")
    public <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }

        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else {
            return JSON.toJavaObject(JSON.parseObject(value),clazz);
        }
    }
    /**
     *
     * @param Every T values
     * @param T 任意类型
     * @return String
     */
    @Override
    public <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }
}

package com.yuepai.yuepaiserver.service.redis.impl;/*
 * @author p78o2
 * @date 2019/7/31
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.yuepai.yuepaiserver.service.redis.RedisTemplateService;
import com.yuepai.yuepaiserver.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Service(version = "1.0.0",timeout = 10000,loadbalance ="roundrobin")
public class RedisTemplateServiceImpl implements RedisTemplateService{
    RedisUtil redisUtil = new RedisUtil();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public <T> boolean set(final String key, final String value) {
        try{
//            redisUtil.set(key,value);
//            redisTemplate.opsForValue().set(key,value);
            boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    connection.set(serializer.serialize(key), serializer.serialize(value));
                    return true;
                }
            });
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object get(final String key) {
//      return key == null ?null : redisTemplate.opsForValue().get(key);
//        return key == null ?null : redisUtil.get(key);
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

}

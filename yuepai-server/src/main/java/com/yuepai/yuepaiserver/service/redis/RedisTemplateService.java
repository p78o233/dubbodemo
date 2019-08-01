package com.yuepai.yuepaiserver.service.redis;/*
 * @author p78o2
 * @date 2019/7/31
 */
//redis基础操作类
public interface RedisTemplateService {
    public <T> boolean set(String key ,T value);
    public Object get(String key);
}

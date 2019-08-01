package com.yuepai.yuepaiserver.utils;/*
 * @author p78o2
 * @date 2019/7/31
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//redis 工具类
@Component
public final class RedisUtil {
    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;
//    ======================================common================================================================
//    指定缓存失效时间 key 键  time 时间（秒）
    public boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    根据key获取过期时间 秒 0代表永久有效
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }
//    判断key是否存在
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    删除缓存 keys 可以传一个或者多个
    public boolean del(String... keys){
        if(keys!=null&&keys.length>0){
            if(keys.length==0){
                return redisTemplate.delete(keys[0]);
            }else{
                if(redisTemplate.delete((CollectionUtils.arrayToList(keys)))>0)
                    return true;
                return false;
            }
        }
        return false;
    }
//    =================================================String=======================================================
//    普通字符串获取
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

//    普通字符串存入
    public boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    普通传入并设置时间 time 时间单位秒 设置小于等于0永久
    public boolean set(String key,String value,long time){
        try {
            if(time>0) {
                redisTemplate.opsForValue().set(key, value, time);
            }else{
                set(key,value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    递增  delta 要增加多少
    public long incr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }
//    递减  delta 要递减多少
    public long decr(String key,long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key,delta);
    }
//    =========================================Map====================================================
//    获取hashmap 里面的值  key 键（不能为空） item 项(不能为空)
    public Object hashItemGet(String key,String item){
        return redisTemplate.opsForHash().get(key,item);
    }
//    获取hashkey对应的键值
    public Map<Object,Object> hashMapGet(String key){
        return redisTemplate.opsForHash().entries(key);
    }
//    hashmap存入
    public boolean hashMapSet(String key,Map<Object,Object>map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    hashMap存入并设置时间 time 时间（秒）
    public boolean hashMapSet(String key,Map<Object,Object>map,long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    向一张hash表中插入数据，如果不存在的将被创建，key键  item项，value值
    public boolean hashMapTableSet(String key,String item,Object value){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //    向一张hash表中插入数据，如果不存在的将被创建，key键  item项，value值,time 时间秒，如果已存在时间的会覆盖原有的时间
    public boolean hashMapTableSet(String key,String item,Object value,long time){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    删除hash表中的值 key 键 items 项可以多个，参数都不能为空
    public void hashMapDelete(String key,Object... items){
        redisTemplate.opsForHash().delete(key,items);
    }
//    判断hash表中是否有该项的值 key 键 item 项，都不能为空
    public boolean hasHashKeyItem(String key,String item){
        return redisTemplate.opsForHash().hasKey(key,item);
    }
//    hash项递增 如果项不存在，就会创建一个然后返回
    public double hashItemIncr(String key,String item,double delta){
        return redisTemplate.opsForHash().increment(key,item,delta);
    }
    //    hash项递减 如果项不存在，就会创建一个然后返回
    public double hashItemDecr(String key,String item,double delta){
        return redisTemplate.opsForHash().increment(key,item,-delta);
    }
//    ===========================================set======================================================
//    根据key 获取set中所有值
    public Set<Object> getSet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
//    根据value去set中查找是否存在
    public boolean isExistSet(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    将数据放入set缓存 key键 values 可以是多个 返回成功的个数
    public long setSet(String key,Object... values){
        try {
            return redisTemplate.opsForSet().add(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    //    将数据放入set缓存 key键 values 可以是多个 time 时间（秒） 返回成功的个数
    public long setAndTimeSet(String key,long time,Object... values){
        try {
            long count =  redisTemplate.opsForSet().add(key,values);
            if(time>0){
                expire(key,time);
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
//    获取set的长度
    public long getSetSize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
//    移除值为values 的，可以有多个
    public long removeSet(String key,Object... values){
        try{
            return redisTemplate.opsForSet().remove(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
//    ======================================================list==========================================
//    获取list缓存 key 键  start 开始 end 结束     start传0 end传-1 就是查询全部
    public List<Object> getList(String key,long start,long end){
        try {
            return redisTemplate.opsForList().range(key,start,end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
//    获取lis的长度
    public long getListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
//    通过索引获取list中的值 key 键 index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；
//    index<0时，-1，表尾，-2倒数第二个元素，依次类推
    public Object getListIndex(String key,long index){
        try{
            return redisTemplate.opsForList().index(key,index);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
//    将list放入缓存
    public boolean setList(String key,Object value){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //    将list放入缓存 time(时间：秒)
    public boolean setList(String key,Object value,long time){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            if(time>0)
                expire(key,time);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //    将list放入缓存
    public boolean setList(String key,List<Object> values){
        try {
            redisTemplate.opsForList().rightPushAll(key,values);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //    将list放入缓存 time(时间：秒)
    public boolean setList(String key,List<Object> values,long time){
        try {
            redisTemplate.opsForList().rightPushAll(key,values);
            if(time>0)
                expire(key,time);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    根据索引修改list中的某条数据 key 键  index 索引  value 新修改的的值
    public boolean updateListByIndex(String key,long index,Object value){
        try {
            redisTemplate.opsForList().set(key,index,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    移除n个值为value的 key 键 count 移除个数 value 值 return 移除的个数
    public long removeListByValue(String key,long count,Object value){
        try {
            return redisTemplate.opsForList().remove(key,count,value);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}

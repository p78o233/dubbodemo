package com.yuepai.yuepaiserver.service.test;

import com.yuepai.yuepaiserver.entity.po.Test;

import java.util.List;

public interface TestService {
    public List<Test> getAll();

//    数据中心号：datacenterId，机器号
    public long getSnowflakeId(int datacenterId,int workerId);
}

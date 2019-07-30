package com.yuepai.yuepaiserver.service.test.impl;

import com.alibaba.dubbo.config.annotation.Method;
import com.alibaba.dubbo.config.annotation.Service;
import com.yuepai.yuepaiserver.entity.po.Test;
import com.yuepai.yuepaiserver.mapper.TestMapper;
import com.yuepai.yuepaiserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
@Service(version = "1.0.0",timeout = 10000,loadbalance ="roundrobin")
public class TestServiceImpl  implements TestService{
    @Autowired
    private TestMapper testMapper;
    @Override
    public List<Test> getAll() {
        return testMapper.getAllTest();
    }
}

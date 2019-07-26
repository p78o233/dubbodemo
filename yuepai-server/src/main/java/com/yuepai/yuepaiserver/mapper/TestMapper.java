package com.yuepai.yuepaiserver.mapper;

import com.yuepai.yuepaiserver.entity.po.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//测试用
@Mapper
public interface TestMapper {
    @Select("select * from test")
    public List<Test> getAllTest();
}

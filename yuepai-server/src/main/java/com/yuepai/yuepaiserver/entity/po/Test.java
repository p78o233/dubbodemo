package com.yuepai.yuepaiserver.entity.po;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class Test implements Serializable {
    @ApiModelProperty(value = "测试表")
    private Integer id;
    @ApiModelProperty(value = "名字")
    private String name;

    public Test(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Test() {
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

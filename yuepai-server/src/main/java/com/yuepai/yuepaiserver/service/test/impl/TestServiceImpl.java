package com.yuepai.yuepaiserver.service.test.impl;

import com.alibaba.dubbo.config.annotation.Method;
import com.alibaba.dubbo.config.annotation.Service;
import com.yuepai.yuepaiserver.entity.po.Test;
import com.yuepai.yuepaiserver.mapper.TestMapper;
import com.yuepai.yuepaiserver.service.test.TestService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service(version = "1.0.0",timeout = 10000,loadbalance ="roundrobin")
public class TestServiceImpl  implements TestService{
    @Autowired
    private TestMapper testMapper;
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Override
    public List<Test> getAll() {
        return testMapper.getAllTest();
    }


    /**
     *  同时监听 topic 的消息了，可同时监听多个topic
     * @param record
     * @throws Exception
     */
    @KafkaListener(topics = {"test"})
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            logger.info("消费者开始消费message：" + message);
        }
    }
}

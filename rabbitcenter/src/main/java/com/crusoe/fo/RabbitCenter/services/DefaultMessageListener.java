package com.crusoe.fo.RabbitCenter.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;
import com.crusoe.fo.RabbitCenter.channel.OrderChannel;

@Component
@Slf4j
public class DefaultMessageListener {

    @RabbitListener(OrderChannel.saveOrder)
    public void processMyMessage(String message) {
        log.info("接收到消息：" + message);
    }
}
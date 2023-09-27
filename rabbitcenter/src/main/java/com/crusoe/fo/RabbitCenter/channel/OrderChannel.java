package com.crusoe.fo.RabbitCenter.channel;

//import org.springframework.cloud.stream.binder.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Auther: suruozhong
 * @Date: 2019/9/17 15:45
 * @Description:
 */
public interface OrderChannel {
    //定义通道的名称
    String saveOrder = "saveOrder";
    //定义为输入通道
    @Input (saveOrder)
    SubscribableChannel saveOrder();

}

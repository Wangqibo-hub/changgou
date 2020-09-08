package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-25 20:22
 * @description 支付消息监听类
 */
@Component
@RabbitListener(queues = "queue.order")
public class OrderPayMessageListener {

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler//方法用处理监听到queue.order队列的消息  可靠性消息
    public void handler(String msg) {
        System.out.println(msg);
        //1.获取消息本身
        //2.将其转换成MAP对象
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        if (map != null) {
            String out_trade_no = map.get("out_trade_no");
            //3.判断是否支付成功 如果成功 则 更新状态
            if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                //1.获取订单号 更新数据到数据库
                Order order = orderMapper.selectByPrimaryKey(out_trade_no);
                order.setPayStatus("1");//设置支付柱状图为1
                String time_end = map.get("time_end");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date parse = simpleDateFormat.parse(time_end);
                    order.setPayTime(parse);//付款时间
                    order.setUpdateTime(parse);
                    order.setTransactionId(map.get("transaction_id"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                orderMapper.updateByPrimaryKeySelective(order);
            } else {
                Order order = orderMapper.selectByPrimaryKey(out_trade_no);
                order.setIsDelete("1");//删除
                //4.如果失败  删除相关信息 为了简单我们 删除掉订单
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }
    }
}

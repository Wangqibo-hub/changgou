package com.changgou.service.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.service.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-25 18:01
 * @description 微信支付控制层
 */
@RestController
@RequestMapping("/weixin/pay")
@CrossOrigin
public class WeixinPayController {
    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private Environment environment;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
    * @Description: 生成二维码
    * @Param: [out_trad_no, total_fee]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/25/0025
    */
    @RequestMapping("create/native")
    public Result createNative(@RequestParam Map<String, String> paramMap){
        Map<String, String> resultMap = weixinPayService.createNative(paramMap);
        return new Result(true, StatusCode.OK, "生成二维码成功", resultMap);
    }

    /**
    * @Description: 查询订单状态
    * @Param: [out_trade_no]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/25/0025
    */
    @RequestMapping("status/query")
    public Result queryPayStatus(String out_trade_no){
        Map resultMap = weixinPayService.queryPayStatus(out_trade_no);
        return new Result(true, StatusCode.OK, "查询订单状态成功", resultMap);
    }

    /**
    * @Description: 支付回调
    * @Param: [request]
    * @Return: java.lang.String
    * @Author: Wangqibo
    * @Date: 2020/8/25/0025
    */
    @RequestMapping("notify/url")
    public String notifyUrl(HttpServletRequest request){
        String resultxml = null;
        //接收数据流
        ServletInputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = request.getInputStream();
            //将数据流转出字节数组
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int leng = 0;
            while ((leng = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, leng);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();//所的数据的字节数组

            //字节数组 转成字符串
            String string = new String(bytes, "utf-8");

            //xml转成MAP ---》业务处理。
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(string);

            System.out.println(stringStringMap);

            String attach = stringStringMap.get("attach");//参数列表数据JSON
            Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
            String from = attachMap.get("from");//获取标识
            switch (from){
                case "1":
                    System.out.println("发送普通队列");
                    //发送消息
                    rabbitTemplate.convertAndSend(
                            environment.getProperty("mq.pay.exchange.order"),
                            environment.getProperty("mq.pay.routing.key"),
                            JSON.toJSONString(stringStringMap)
                    );
                    break;
                case "2":
                    System.out.println("发送秒杀队列");
                    rabbitTemplate.convertAndSend(
                            environment.getProperty("mq.pay.exchange.seckillorder"),
                            environment.getProperty("mq.pay.routing.seckillkey"),
                            JSON.toJSONString(stringStringMap)
                    );
                    break;
                default:
                    System.out.println("错误的信息");
                    break;
            }
            //按照微信的规定返回响应结果给微信支付系统
            Map<String, String> resultmap = new HashMap<>();
            resultmap.put("return_code", "SUCCESS");
            resultmap.put("return_msg", "OK");
            resultxml = WXPayUtil.mapToXml(resultmap);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return resultxml;
    }
}

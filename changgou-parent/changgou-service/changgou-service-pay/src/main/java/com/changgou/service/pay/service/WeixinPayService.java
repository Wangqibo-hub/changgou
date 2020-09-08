package com.changgou.service.pay.service;

import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-25 18:00
 * @description 微信支付服务接口
 */
public interface WeixinPayService {

    Map<String, String> createNative(Map<String, String> paramMap);

    Map queryPayStatus(String out_trade_no);
}

package com.changgou.service.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.service.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-25 18:00
 * @description 微信服务接口实现类
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;
    
    /**
    * @Description: 创建二维码
    * @Param: [out_trad_no, total_fee]
    * @Return: java.util.Map<java.lang.String,java.lang.String>
    * @Author: Wangqibo
    * @Date: 2020/8/25/0025
    */
    @Override
    public Map<String, String> createNative(Map<String, String> paramMap) {
        try {
            //1.封装参数
            Map param = new HashMap();
            param.put("appid", appid);                              //应用ID
            param.put("mch_id", partner);                           //商户ID号
            param.put("nonce_str", WXPayUtil.generateNonceStr());   //随机数
            param.put("body", "畅购de商品");                         //订单描述
            param.put("out_trade_no",paramMap.get("out_trade_no"));   //商户订单号
            param.put("total_fee", paramMap.get("total_fee"));        //交易金额
            param.put("spbill_create_ip", "127.0.0.1");           //终端IP
            param.put("notify_url", notifyurl);                    //回调地址
            param.put("trade_type", "NATIVE");                     //交易类型
            param.put("attach", JSON.toJSONString(paramMap));

            //2.将参数转换成xml字符，携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            //3.执行请求，微信生成支付连接
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(paramXml);
            client.post();

            //4.获取返回参数
            String content = client.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap"+stringMap);

            //5.获取部分页面所需参数
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("code_url",stringMap.get("code_url"));
            dataMap.put("out_trade_no",paramMap.get("out_trade_no"));
            dataMap.put("total_fee",paramMap.get("total_fee"));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @Description: 查询订单状态
    * @Param: [out_trade_no]
    * @Return: java.util.Map
    * @Author: Wangqibo
    * @Date: 2020/8/25/0025
    */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //1.封装参数
            Map param = new HashMap();
            param.put("appid",appid);                            //应用ID
            param.put("mch_id",partner);                         //商户号
            param.put("out_trade_no",out_trade_no);              //商户订单编号
            param.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符
            //2.将参数转换成xml
            String paramXml = WXPayUtil.generateSignedXml(param,partnerkey);
            //3.发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            //4.获取返回结果
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

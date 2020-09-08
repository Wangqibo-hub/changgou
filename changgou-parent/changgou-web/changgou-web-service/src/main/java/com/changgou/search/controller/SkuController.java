package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-18 17:09
 * @description 商品搜索微服务
 */
@Controller
@RequestMapping("search")
public class SkuController {
    //注入SkuFeign
    @Autowired
    private SkuFeign skuFeign;

    /**
     * @Description: 商品搜索
     * @Param: [searchMap, model]
     * @Return: java.lang.String
     * @Author: Wangqibo
     * @Date: 2020/8/18/0018
     */
    @GetMapping("list")
    public String search(@RequestParam(required = false) Map searchMap, Model model) {
        //调用changgou-service-search微服务，获取搜索结果
        Map resultMap = skuFeign.search(searchMap);
        //搜索数据结果
        model.addAttribute("result", resultMap);
        //搜索数据条件
        model.addAttribute("searchMap", searchMap);
        //请求路径url
        String url = url(searchMap);
        model.addAttribute("url", url);
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.valueOf(resultMap.get("totalPages").toString()),
                Integer.valueOf(resultMap.get("pageNum").toString()),
                Integer.valueOf(resultMap.get("pageSize").toString()));
        model.addAttribute("page", page);
        return "search";
    }

    /**
     * @Description: 拼接url
     * @Param: [searchMap]
     * @Return: java.lang.String
     * @Author: Wangqibo
     * @Date: 2020/8/18/0018
     */
    private String url(Map<String, String> searchMap) {
        StringBuffer url = new StringBuffer("/search/list");
        if (searchMap != null && searchMap.size() > 0) {
            url.append("?");
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase("sortField")
                        || key.equalsIgnoreCase("sortRule")
                        || key.equalsIgnoreCase("pageNum")) {
                    continue;
                }
                url.append(key);
                url.append("=");
                url.append(value);
                url.append("&");
            }
            //去掉最后一个&
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }
}

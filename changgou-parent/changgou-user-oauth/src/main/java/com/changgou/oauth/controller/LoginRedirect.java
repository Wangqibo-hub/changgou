package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-23 10:28
 * @description 登陆跳转
 */
@Controller
@RequestMapping("oauth")
public class LoginRedirect {

    @RequestMapping("login")
    public String login(@RequestParam(value = "urlBack",required = false,defaultValue = "") String url, Model model){
        model.addAttribute("url", url);
        return "login";
    }
}

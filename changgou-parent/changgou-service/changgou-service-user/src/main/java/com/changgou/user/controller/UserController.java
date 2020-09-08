package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.ImageProducer;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController extends ICoreControllerImpl<User> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService, User.class);
        this.userService = userService;
    }

    /**
     * @Description: 用户登录
     * @Param: [username, password]
     * @Return: entity.Result
     * @Author: Wangqibo
     * @Date: 2020/8/19/0019
     */
    @GetMapping("login")
    public Result login(String username, String password, HttpServletResponse response) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new Result(false, StatusCode.ERROR, "用户名或密码不能为空！");
        }
        User user = userService.selectByPrimaryKey(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            //设置令牌信息
            HashMap<String, Object> info = new HashMap<>();
            info.put("role", "USER");
            info.put("username", username);
            //生成令牌
            String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);
            //存入cookie
            Cookie cookie = new Cookie("Authorization",jwt);
            response.addCookie(cookie);
            return new Result(true, StatusCode.OK, "登录成功",jwt);

        }
        return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Result<List<User>> selectAll() {
        return super.selectAll();
    }

    @GetMapping("load/{id}")
    public Result<User> selectByPrimaryKey(@PathVariable("id") Object key){
        User user = userService.selectByPrimaryKey(key);
        return new Result<User>(true, StatusCode.OK, "查询用户成功", user);
    }

    /**
    * @Description: 增加积分
    * @Param: [username, points]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/23/0023
    */
    @GetMapping("/points/add")
    public Result addPoints(@RequestParam("username") String username, @RequestParam("points") Integer points){
        Integer count = userService.addPoints(username,points);
        if (count > 0) {
            return new Result(true, StatusCode.OK, "增加积分成功");
        }else{
            return new Result(false, StatusCode.ERROR, "增加积分失败");
        }
    }
}


package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import entity.StatusCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-22 17:29
 * @description 用户feign
 */
@FeignClient(value = "user",path = "user")
public interface UserFeign {

    @GetMapping("load/{id}")
    Result<User> selectByPrimaryKey(@PathVariable("id") Object key);

    @GetMapping("/points/add")
    Result addPoints(@RequestParam("username") String username, @RequestParam("points") Integer points);
}

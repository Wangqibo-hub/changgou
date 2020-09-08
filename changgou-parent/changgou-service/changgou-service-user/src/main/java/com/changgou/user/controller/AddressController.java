package com.changgou.user.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController extends ICoreControllerImpl<Address>{

    private AddressService  addressService;
    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    public AddressController(AddressService  addressService) {
        super(addressService, Address.class);
        this.addressService = addressService;
    }

    /**
    * @Description: 根据用户名查询地址信息
    * @Param: []
    * @Return: entity.Result<java.util.List<com.com.changgou.user.pojo.Address>>
    * @Author: Wangqibo
    * @Date: 2020/8/23/0023
    */
    @GetMapping("user/list")
    public Result<List<Address>> list(){
        //获取用户名
        String username = tokenDecode.getUsername();
        //查询用户收件地址
        List<Address> addressList = addressService.list(username);
        return new Result<>(true, StatusCode.OK, "查询地址成功", addressList);
    }
}

package com.changgou.goods.service.impl;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:25
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandServiceImplTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void test01(){
        Brand brand = brandService.selectByPrimaryKey(1115);
        System.out.println(brand.getName());
    }

}

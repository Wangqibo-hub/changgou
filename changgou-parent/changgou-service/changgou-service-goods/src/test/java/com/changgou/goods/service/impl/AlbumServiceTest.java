package com.changgou.goods.service.impl;

import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:40
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlbumServiceTest {

    @Autowired
    private AlbumService albumService;

    @Test
    public void test02(){
        PageInfo byPage = albumService.findByPage(1, 2);
        System.out.println(byPage.getList());
    }
}

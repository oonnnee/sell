package com.imooc.sell.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatMpConfigTest {

    @Autowired
    private WeChatMpConfig weChatMpConfig;

    @Autowired
    private String string;

    @Test
    public void string(){
        System.out.println(string);
    }

}
package com.imooc.sell.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.SocketTimeoutException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatAccountConfigTest {

    @Autowired
    private WeChatAccountConfig weChatAccountConfig;

    @Test
    public void basic(){
        System.out.println(weChatAccountConfig.getMpAppId());
        System.out.println(weChatAccountConfig.getMpAppSecret());
    }

}
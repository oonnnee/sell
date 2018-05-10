package com.imooc.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellApplicationTests {

    @Test
    public void log() {

        Logger logger = LoggerFactory.getLogger(SellApplicationTests.class);

        logger.error("[log test] error...");
        logger.warn("[log test] warn...");
        logger.info("[log test] info...");
        logger.debug("[log test] debug...");
        logger.trace("[log test] trace...");

        log.error("[log slf4j test] error...");
        log.warn("[log slf4j test] warn...");
        log.info("[log slf4j test] info...");
        log.debug("[log slf4j test] debug...");
        log.trace("[log slf4j test] trace...");

    }

}

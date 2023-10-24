package com.wth.ff.once;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *  定时任务功能测试
 */
@SpringBootTest
public class Sechduled {

    @Test
    @Scheduled(cron = "0/2 * * * *")
    public void test1() {

        System.out.println("定时任务执行了" + Thread.currentThread());
    }

}

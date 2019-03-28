package com.example.transactionjms;

import com.example.transactionjms.model.AdminMessage;
import com.example.transactionjms.service.AdminMessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TransactionJmsApplicationTests {

    @Autowired
    AdminMessageService adminMessageService;
    @Before
    public void contextLoads() {
        int cnt = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(cnt);
        final Semaphore semaphore = new Semaphore(1);

        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            executorService.submit(()->{
                try {
                    AdminMessage adminMessage = new AdminMessage();
                    adminMessage.setId(Long.valueOf(finalI));
                    adminMessage.setCtime(new Date());
                    adminMessage.setStatus(0);
                    adminMessage.setUtime(new Date());
                    adminMessage.setContent("db");
                    adminMessage.setMessageType(0);
                    adminMessage.setNeedOpen(false);
                    adminMessage.setTitle("");
                    log.info("============计数{}",finalI);
                    semaphore.acquire();
                    log.info("============获取锁{}",finalI);
                    long time = System.currentTimeMillis();
                    adminMessageService.insertMaster(adminMessage);
                    log.info("============耗时{}",System.currentTimeMillis()-time);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }finally {
                    semaphore.release();
                }
            });
        }
        executorService.shutdown();
    }

    @Test
    public void test(){

    }
}

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
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class TransactionJmsApplicationTests {

    @Autowired
    AdminMessageService adminMessageService;
    @Before
    public void contextLoads() {
        int cnt = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(cnt);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            executorService.submit(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i1 = 0; i1 < 100; i1++) {
                        restTemplate.getForObject("10.10.13.21:9999/jms/send3/1",String.class);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }finally {
                }
            });
        }
        countDownLatch.countDown();
        executorService.shutdown();
    }

    @Test
    public void test(){

    }

    public static void main(String[] args) {
//        Integer i1 = Integer.valueOf(2);
//        Integer i2 = Integer.valueOf(2);
//
//        System.out.println(i1 == i2);
//
//        i1 = 3;
//        i2 = 3;
//
//        System.out.println(i1 == i2);
//
//        String a1 = String.valueOf("aaa");
//        String a2 = String.valueOf("aaa");
//        System.out.println(a1 == a2);
//
//        a1 = "bbb";
//        a2 = "bbb";
//        System.out.println(a1 == a2);
//
//        a1 = new String("aaa");
//        a2 = new String("aaa");
//        System.out.println(a1 == a2);

        int cnt = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(cnt);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            executorService.submit(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i1 = 0; i1 < 10; i1++) {
                        long currentTimeMillis = System.currentTimeMillis();
                        restTemplate.getForObject("http://10.10.13.21:9999/jms/send3/1",String.class);
                        System.out.println("********************************************"+(System.currentTimeMillis() - currentTimeMillis));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }finally {
                }
            });
        }
        countDownLatch.countDown();
        executorService.shutdown();

    }
}

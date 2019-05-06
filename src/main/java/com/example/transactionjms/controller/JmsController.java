package com.example.transactionjms.controller;

import com.example.transactionjms.dto.UserDto;
import com.example.transactionjms.model.AdminMessage;
import com.example.transactionjms.service.AdminMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import java.util.Date;
import java.util.concurrent.*;

@RestController
@RequestMapping("jms")
@Slf4j
public class JmsController {

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("testQueue1")
    Queue queue;

    @Autowired
    @Qualifier("testQueue2")
    Queue queue2;

    @Autowired
    AdminMessageService adminMessageService;


    @RequestMapping("/send/{id}")
    public UserDto sendMq(@PathVariable String id){
        UserDto userDto= new UserDto();
        userDto.setId(id);
        userDto.setName("name"+id);
        jmsMessagingTemplate.convertAndSend(queue,userDto);
        return userDto;
    }

    @RequestMapping("/send2/{id}")
    public UserDto sendMq2(@PathVariable String id){
        UserDto userDto= new UserDto();
        userDto.setId(id);
        userDto.setName("name"+id);
        jmsMessagingTemplate.convertAndSend(queue2,userDto);
        return userDto;
    }

    @RequestMapping("/send3/{id}")
    public AdminMessage sendMq3(@PathVariable String id){
        log.info("*******进入请求");
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setId(Long.valueOf(id));
        adminMessage.setCtime(new Date());
        adminMessage.setStatus(0);
        adminMessage.setUtime(new Date());
        adminMessage.setContent("db");
        adminMessage.setMessageType(0);
        adminMessage.setNeedOpen(false);
        adminMessage.setTitle("");
        long time = System.currentTimeMillis();
        adminMessageService.insertMaster(adminMessage);
        log.info("============耗时{}",System.currentTimeMillis()-time);
        return adminMessage;
    }

    @RequestMapping("/test/{cnt}")
    public long test(@PathVariable int cnt){
        long sum=0;
        for (int i = 1; i < cnt+1; i++) {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setId(Long.valueOf(i));
            adminMessage.setCtime(new Date());
            adminMessage.setStatus(0);
            adminMessage.setUtime(new Date());
            adminMessage.setContent("db");
            adminMessage.setMessageType(0);
            adminMessage.setNeedOpen(false);
            adminMessage.setTitle("");
            long time = System.currentTimeMillis();
            adminMessageService.insertMaster(adminMessage);
            sum+=( System.currentTimeMillis() - time);
        }
        return sum/cnt;
    }
}

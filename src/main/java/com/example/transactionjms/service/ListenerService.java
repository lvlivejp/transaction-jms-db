package com.example.transactionjms.service;
import java.util.Date;

import com.example.transactionjms.dto.UserDto;
import com.example.transactionjms.model.AdminMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

@Service
@Slf4j
public class ListenerService {

    @Autowired
    AdminMessageService adminMessageService;

    @JmsListener(destination = "testQueue1")
    public void receiveMessage(UserDto userDto) {
        log.info("接收到消息{}",userDto);
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setId(Long.valueOf(userDto.getId()));
        adminMessage.setCtime(new Date());
        adminMessage.setStatus(0);
        adminMessage.setUtime(new Date());
        adminMessage.setContent(userDto.getName());
        adminMessage.setMessageType(0);
        adminMessage.setNeedOpen(false);
        adminMessage.setTitle("");
        adminMessageService.insertMaster(adminMessage);
        if(userDto.getId().equals("9")){
            throw new RuntimeException("jms error");
        }
    }
}

package com.example.transactionjms.service;

import com.example.transactionjms.dao.AdminMessageMapper;
import com.example.transactionjms.dto.UserDto;
import com.example.transactionjms.model.AdminMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;

@Service
public class AdminMessageService {

    @Autowired
    private AdminMessageMapper adminMessageMapper;

    @Autowired
    @Qualifier("testQueue2")
    Queue queue2;

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    public List<AdminMessage> selectAdminMaster(){
        return adminMessageMapper.selectAll();
    }

    @Transactional
    public void insertMaster(AdminMessage adminMessage) {
        adminMessageMapper.insert(adminMessage);

        UserDto userDto= new UserDto();
        userDto.setId(adminMessage.getId()+"");
        userDto.setName("name"+adminMessage.getId());
        jmsMessagingTemplate.convertAndSend(queue2,userDto);

//        if(adminMessage.getId() == 9){
//            throw new RuntimeException("db error");
//        }
    }

}

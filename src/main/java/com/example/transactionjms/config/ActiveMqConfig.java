package com.example.transactionjms.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@EnableJms
@Configuration
public class ActiveMqConfig {

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @ConfigurationProperties("spring.jms.listener")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        /**
         * 设置为true时，当消息消费失败后，会告诉mq服务器该消息没有消费成功，会继续投递N次。（N为配置次数）
         * 设置为false时，失败后不再进行重复投递。
         */
        factory.setSessionTransacted(true);
        /**
         * 发送时自动将JavaBean转成json格式
         * 接收时自动将json格式转为JavaBean
         */
        factory.setMessageConverter(jacksonJmsMessageConverter());
        /**
         * 将数据库的事务和Listener事务，消息发送事务捆绑，全部成功后才提交。
         * 否则全部回滚
         */
        factory.setTransactionManager(platformTransactionManager);
        factory.setReceiveTimeout(10000L);
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate= new JmsTemplate(connectionFactory);
        /**
         * 发送消息和数据的事务绑定，一同提交或回滚
         * 性能几乎没影响
         */
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }
    @Bean("testQueue1")
    public Queue testQueue1(){
        return new ActiveMQQueue("testQueue1");
    }

    @Bean("testQueue2")
    public Queue testQueue2(){
        return new ActiveMQQueue("testQueue2");
    }


}

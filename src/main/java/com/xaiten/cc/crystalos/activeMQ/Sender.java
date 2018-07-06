package com.xaiten.cc.crystalos.activeMQ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

    public static void main(String[] args) {
         //连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        //JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // 一个发送或接收消息的线程
        Session session;
        //消息的目的地;消息发送给谁.
        List<Destination> destinations = new ArrayList<>();
        
        // 消费者，消息接收者
        List<MessageProducer> messageProducers = new ArrayList<>();
        try {
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");//用户名密码必须和activemq.xml中的用户名和密码一致，否则不能连接
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();//从工厂创建一个连接

            connection.start();//开启
             // 创建一个session，
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个队列，队列名随意定义
            Destination destination1 = session.createQueue("FirstQueuess");//队列式
            
            Destination destination2 = session.createQueue("SecondQueuess");
            
            //destination = session.createTopic("mmm");//主题式
            // 获取消息生成者
           
            MessageProducer messageProducer1 = session.createProducer(destination1);
            MessageProducer messageProducer2 = session.createProducer(destination2);
            // 设置持久化，此处学习，根据需要设置
            messageProducer1.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer2.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            destinations.add(destination1);
            destinations.add(destination2);
            messageProducers.add(messageProducer1);
            messageProducers.add(messageProducer2);
            //消息发布--队列形式
            messageProducers.add(messageProducer1);
            //发送100条信息
            for(int i = 0 ; i < 50 ; i ++){
                 // 发送消息到目的地方
                TextMessage tm = session.createTextMessage("发送的信息 "+i);
                System.out.println("Producer"+(i%2)+" 发送的信息 "+i);
                messageProducers.get(i%2).send(tm);
            }
            session.commit();

        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if(connection != null){
                    connection.close();//
                }
            } catch (JMSException e) {
                System.out.println("----");
            }
        }
    }
}
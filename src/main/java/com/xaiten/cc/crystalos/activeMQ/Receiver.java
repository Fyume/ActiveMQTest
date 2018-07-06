package com.xaiten.cc.crystalos.activeMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver{
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        Destination destination2;
        MessageConsumer messageConsumer ;
        MessageConsumer messageConsumer2 ;
        int i = 0;
        //连接工厂要和生产者一致
        connectionFactory = new ActiveMQConnectionFactory("admin"/*ActiveMQConnection.DEFAULT_USER*/,
                "admin"/*ActiveMQConnection.DEFAULT_PASSWORD*/, "tcp://localhost:61616");

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //使用的队列和生产者定义的一致，才能接收生产者发送的消息
            destination = session.createQueue("FirstQueuess");
            destination2 = session.createQueue("SecondQueuess");
            //destination = session.createTopic("mmm");//主题式
            messageConsumer = session.createConsumer(destination);
            messageConsumer2 = session.createConsumer(destination2);
            //这里一次性全部获取
            
            while(true){
                TextMessage tm = (TextMessage) messageConsumer.receive(500000);
                if(tm != null){
                    System.out.println(i+"----Consumer1收到信息："+tm.getText());i++;
                }
                TextMessage tm2 = (TextMessage) messageConsumer2.receive(500000);
                if(tm2 != null){
                    System.out.println(i+"----Consumer2收到信息："+tm2.getText());i++;
                }
            }
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {  
                if (null != connection)  
                    connection.close();  
            } catch (Throwable ignore) {  
            }  
        }  
    }
}
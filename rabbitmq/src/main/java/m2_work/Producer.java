package m2_work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class Producer {
    public static void main(String[] args)throws Exception {
        //创建连接工厂,并设置连接信息
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        //f.setPort(5672);  //默认端口可以省略
        f.setUsername("admin");
        f.setPassword("admin");

        Channel c=f.newConnection().createChannel();

        c.queueDeclare("helloworld",false,false,false,null);
        while (true) {
            //控制台输入的消息发送到rabbitmq
            System.out.print("输入消息: ");
            String msg = new Scanner(System.in).nextLine();
            //如果输入的是"exit"则结束生产者进程
            if ("exit".equals(msg)) {
                break;
            }
            //参数:exchage,routingKey,props,body
            c.basicPublish("", "helloworld", null, msg.getBytes());
            System.out.println("消息已发送: "+msg);
        }

        c.close();
    }
}

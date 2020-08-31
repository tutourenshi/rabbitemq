package m4_routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Consumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c=f.newConnection().createChannel();
        /* 参数含义:
         *   -queue: 队列名称
         *   -durable: 队列持久化,true表示RabbitMQ重启后队列仍存在
         *   -exclusive: 排他,true表示限制仅当前连接可用
         *   -autoDelete: 当最后一个消费者断开后,是否删除队列
         *   -arguments: 其他参数
         */
        c.exchangeDeclare("direct_logs",BuiltinExchangeType.DIRECT);
        String queue=c.queueDeclare().getQueue();
        System.out.print("输入绑定键，空格隔开");
        String s=new Scanner(System.in).nextLine();
        String[] a=s.split("\\s+");
        for (String key:a)
            c.queueBind(queue,"direct_logs",key);
        // 正常的消费数据
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                byte[] a = message.getBody();
                String msg = new String(a);
                String key = message.getEnvelope().getRoutingKey();
                System.out.println("收到： "+msg+",  key="+key);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
            }
        };
        //3. 消费数据
        c.basicConsume(queue,
                true,
                deliverCallback,
                cancelCallback);

    }
}

package m5_topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Consumer {
    public static void main(String[] args) throws Exception {
        // 连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140"); // www.wht6.cn
        // f.setPort(5672); //默认端口可以省略
        f.setUsername("admin");
        f.setPassword("admin");
        // f.setVirtualHost("/wht");

        Channel c = f.newConnection().createChannel();

        // 1.定义交换机  2.定义随机队列  3.用绑定键来帮顶
        c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
        String queue = c.queueDeclare().getQueue();
        System.out.print("输入绑定键，用空格隔开：");
        String s = new Scanner(System.in).nextLine();
        String[] a = s.split("\\s+");
        for (String key: a) {
            c.queueBind(queue, "direct_logs", key);
        }

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

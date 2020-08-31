package m4_routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Channel c = f.newConnection().createChannel();

        // 定义直连交换机
        c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
        // 向交换机发送消息，在消息上需要携带路由键
        while (true) {
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            System.out.print("输入路由键：");
            String key = new Scanner(System.in).nextLine();
            c.basicPublish("direct_logs",
                    key,
                    null,
                    msg.getBytes());
        }

    }
}

package util;

import com.rabbitmq.client.ConnectionFactory;

public class Connutil {
    public void conn(){
        //创建连接工厂,并设置连接信息
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        //f.setPort(5672);  //默认端口可以省略
        f.setUsername("admin");
        f.setPassword("admin");
    }
}

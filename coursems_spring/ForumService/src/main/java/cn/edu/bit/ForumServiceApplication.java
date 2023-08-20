package cn.edu.bit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ForumServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ForumServiceApplication.class,args);
    }
}

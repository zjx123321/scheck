package com.simple.scheck;

import com.alibaba.dubbo.container.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@SpringBootApplication
@ComponentScan
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        Main.main(args);
        SpringApplication.run(Application.class, args);
    }

}

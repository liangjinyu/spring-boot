package cn.nj.ljy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
@ServletComponentScan
@EnableAutoConfiguration
@MapperScan("cn.nj.ljy.mapper")
public class Application {

    public static void main(String[] args) {
        // SpringApplication application = new SpringApplication(Application.class,
        // "classpath*:/applicationContext.xml");
        // application.run(args);

        SpringApplication.run(Application.class, args);
    }
}

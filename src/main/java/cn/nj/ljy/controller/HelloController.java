package cn.nj.ljy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.nj.ljy.entity.Person;
import cn.nj.ljy.third.MessageProducer;

@Controller
@RequestMapping(value = "test")
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Autowired
    private MessageProducer messageProducer;

    @RequestMapping("/hello")
    @ResponseBody
    String hello() {

        LOGGER.info("method hello executed ");
        return "Hello ,spring boot! dataSourceUrl = " + dataSourceUrl + " ,  " + messageProducer.toString();
    }
    
    @RequestMapping("/error")
    @ResponseBody
    private Person error() {

    	    String s = null;
    	    s.split("s");
        return new Person(11,"jack",11);
    }
    
    @RequestMapping("/person")
    @ResponseBody
    private Person person() {
        return new Person(11,"jack",11);
    }

    // public static void main(String[] args) throws Exception {
    // SpringApplication.run(HelloController.class, args);
    // // 运行之后在浏览器中访问：http://localhost:8080/test/hello
    // }

}

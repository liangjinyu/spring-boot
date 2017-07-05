package cn.nj.ljy.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "test")
public class HelloController {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
	
    @RequestMapping("/hello")
    @ResponseBody
    
    String home() {
    	
    	LOGGER.info("method hello executed");
        return "Hello ,spring boot!";
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(HelloController.class, args);
//        // 运行之后在浏览器中访问：http://localhost:8080/test/hello
//    }

}

package cn.nj.ljy;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cn.nj.ljy.controller.HelloController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloTest {

	private MockMvc mvc;
	
	
	@Before(value = "testHello")
	public void setUp() throws Exception{
		mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
	}
	
	@Test
	public void getHello() throws Exception{
		
		ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/test/hello").accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isOk()).andExpect(content().string("Hello ,spring boot! dataSourceUrl = jdbc:mysql://99.48.58.196:3306/merchandisecenter?characterEncoding=utf-8 , MessageProducer [accesskey=aaa, accesssecret=bbb, thirdParam=thirdParamValue]"));
		
	}
}

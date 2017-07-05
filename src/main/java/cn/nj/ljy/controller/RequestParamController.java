package cn.nj.ljy.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;

import cn.nj.ljy.entity.Person;

@Controller
@RequestMapping(value = "request")
public class RequestParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestParamController.class);

	@RequestMapping("/reader")
	@ResponseBody
	String reader(HttpServletRequest request) {

		Person person = null;
		  try {
	            BufferedReader bufferedReader = request.getReader();
	            String str;
	            String wholeStr = "";
	            while ((str = bufferedReader.readLine()) != null) {
	                wholeStr += str;
	            }
	            if (!StringUtils.isEmpty(wholeStr)) {
	            	person = JSON.parse(wholeStr, Person.class);
	            }
	        } catch (Exception e) {
	            LOGGER.error(e.getMessage(),e);
	        }
		return person ==null?"no params":person.toString();
	}
}

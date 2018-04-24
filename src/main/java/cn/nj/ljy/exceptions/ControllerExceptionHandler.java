package cn.nj.ljy.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.nj.ljy.common.LjyResponse;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

	@ExceptionHandler(value=Exception.class)
	public LjyResponse<String> defaultErrorHandler(HttpServletRequest request,Exception e) throws Exception{
		
		
		
		LjyResponse<String> response = new LjyResponse<String>();
		response.setCode("999");
		response.setDesc("系统异常");
		response.setContent(request.getRequestURI()+" error:"+e);
		
		return response;
	}
	
	
}

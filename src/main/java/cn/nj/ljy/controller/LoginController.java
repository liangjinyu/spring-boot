package cn.nj.ljy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.nj.ljy.common.Const;
import cn.nj.ljy.common.LjyResponse;
import cn.nj.ljy.model.vo.login.LoginInfoVO;

@Controller
@RequestMapping(value = "login")
public class LoginController {

	
	
    @RequestMapping("/do")
    @ResponseBody
    public LjyResponse<Boolean> home(@RequestBody LoginInfoVO loginInfo, HttpServletRequest request) {
        LjyResponse<Boolean> response = new LjyResponse<Boolean>();
        if ("admin".equals(loginInfo.getUsername()) && "000000".equals(loginInfo.getPassword())) {
        	
        	HttpSession session = request.getSession(true);
        	session.removeAttribute(Const.USER_SESSION);
        	session.setAttribute(Const.USER_SESSION, loginInfo.getUsername());
            response.setCode("0");
            response.setDesc("success");
        } else {
            response.setCode("1001");
            response.setDesc("用户名或密码错误");
        }
        return response;
    }

}

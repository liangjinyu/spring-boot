package cn.nj.ljy.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.nj.ljy.service.YxServcie;

@Controller
@RequestMapping(value = "yx")
public class YxQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(YxQueryController.class);

    @Autowired
    private YxServcie yxServcie;

    @RequestMapping("/query")
    @ResponseBody
    private String query(HttpServletRequest request) {

        String id = request.getParameter("id");
        String result = "";
        try {
            result = yxServcie.queryById(id);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            result = e.getMessage();
        }
        return result;
    }

    @RequestMapping("/queryCurl")
    @ResponseBody
    private String queryCurl(HttpServletRequest request) {

        String id = request.getParameter("id");
        String result = yxServcie.queryCurl(id);
        return result;
    }

}

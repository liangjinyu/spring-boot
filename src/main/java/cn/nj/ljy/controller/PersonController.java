package cn.nj.ljy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.nj.ljy.common.LjyResponse;
import cn.nj.ljy.common.PageResponse;
import cn.nj.ljy.entity.Person;
import cn.nj.ljy.mapper.PersonMapper;
import cn.nj.ljy.model.vo.person.PersonQueryVO;
import cn.nj.ljy.service.PersonService;
import cn.nj.ljy.service.RedisService;

@Controller
@RequestMapping(value = "person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/select")
    @ResponseBody
    public String select(@RequestBody PersonQueryVO personQueryVO) {

        PageResponse<List<Person>> response = new PageResponse<List<Person>>();
        int totalNum = personService.selectCount(personQueryVO);
        if (totalNum > 0) {
            response.setTotalNum(totalNum);
            int totalPage = (totalNum - 1) / personQueryVO.getPageNum() + 1;
            response.setTotalPage(totalPage);

            List<Person> personList = personService.selectList(personQueryVO);
            response.setContent(personList);
            response.setCode("0");
        }
        return response.toJsonString();
    }

    @RequestMapping("/testTransaction")
    @ResponseBody
    public String testTransaction() {

        String result = personService.addTwoPerson();
        return result;
    }

    @RequestMapping("/add")
    @ResponseBody
    public LjyResponse<String> add(@RequestBody Person person) {
        LjyResponse<String> response = new LjyResponse<String>();
        int affectRows = personService.insert(person);
        response.setContent(affectRows == 1 ? "success" : "failed");
        response.setCode(affectRows == 1 ? "0" : "1");
        redisService.addString(person.getName(), String.valueOf(person.getAge()), 180);
        return response;
    }

    // public static void main(String[] args) throws Exception {
    // SpringApplication.run(HelloController.class, args);
    // // 运行之后在浏览器中访问：http://localhost:8080/test/hello
    // }

}

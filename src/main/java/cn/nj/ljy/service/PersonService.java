package cn.nj.ljy.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.nj.ljy.entity.Person;
import cn.nj.ljy.mapper.PersonMapper;
import cn.nj.ljy.model.vo.person.PersonQueryVO;

@Component
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonMapper personMapper;

    public int selectCount(PersonQueryVO personQueryVO) {
        return personMapper.selectCount(personQueryVO);
    }

    public List<Person> selectList(PersonQueryVO personQueryVO) {
        return personMapper.selectList(personQueryVO);
    }

    public int insert(Person person) {
        return personMapper.insert(person);
    }

    @Transactional
    public String addTwoPerson() {
        
        Person person1 = new Person();
        person1.setName("xx1");
        person1.setAge(11);
        personMapper.insert(person1);

        Person person2 = new Person();
        person2.setName("1234567890123456789012345678901234567890123456789012345678901234567890");
        person2.setAge(11);
        personMapper.insert(person2);
        
        return "success";
    }

}

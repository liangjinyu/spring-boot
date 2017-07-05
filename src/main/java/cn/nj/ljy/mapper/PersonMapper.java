package cn.nj.ljy.mapper;

import java.util.List;

import cn.nj.ljy.entity.Person;
import cn.nj.ljy.model.vo.person.PersonQueryVO;

public interface PersonMapper {

    int insert(Person person);

    List<Person> selectList(PersonQueryVO personQueryVO);

    int selectCount(PersonQueryVO personQueryVO);
}

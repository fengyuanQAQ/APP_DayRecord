package xiaoanju.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xiaoanju.demo.entity.User;
import xiaoanju.demo.mapper.UserMapper;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    void getUserById(){
//        Map<String,Object> map=new HashMap<>();
////        map.put("number","123456");
////        List<User> users = userMapper.selectByMap(map);
////        for (User user : users) {
////            System.out.println(user);
////        }
        User user = userMapper.selectById("123456");
        System.out.println(user);
    }

    @Test
    void getUser(){
        QueryWrapper<User> wrapper=new QueryWrapper<User>();
        wrapper.eq("nickName","xiaoanju");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    void upDate(){
//        User user=new User();
//        user.setId("123456");
//        user.setNickname("caonima");
//        userMapper.updateById(user);

        UpdateWrapper<User> wrapper=new UpdateWrapper<>();
        wrapper.eq("password","123456").set("id","22222");
        userMapper.update(null,wrapper);
    }

    @Test
    void insert(){
        userMapper.insert(new User("4444444","45454","sdfasfa","fdsaafsaf","dsafasf"));
    }

}

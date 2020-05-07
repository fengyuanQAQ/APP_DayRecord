package xiaoanju.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaoanju.demo.entity.User;
import xiaoanju.demo.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //根据用户的id获取用户信息
    public User getUserById(String id,String pass){
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id).eq("password",pass);
        return userMapper.selectOne(wrapper);
    }

    //更改用户的信息
    public void  updateUser(User user){
//        UpdateWrapper<User> wrapper=new UpdateWrapper<>();
//        wrapper.eq("id",)
        userMapper.updateById(user);
    }

    //新增用户
    public void insertUser(User user){
        userMapper.insert(user);
    }

    //检验用户是否存在
    public boolean userExist(String id){
        User user = userMapper.selectById(id);
        if (user != null) {
            return true;
        }else
            return false;
    }

}

package xiaoanju.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaoanju.demo.entity.User;
import xiaoanju.demo.service.UserService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserById")
    public boolean getUserById(@PathParam("id") String id){
        return userService.userExist(id);
    }

    @GetMapping("/getUser")
    public User getUesr( @PathParam("id") String id,@PathParam("password") String password){
        return userService.getUserById(id, password);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user){
        userService.updateUser(user);
        return user;
    }

    @PostMapping("insertUser")
    public User insertUser(@RequestBody User user){
       userService.insertUser(user);
       return user;
    }
}
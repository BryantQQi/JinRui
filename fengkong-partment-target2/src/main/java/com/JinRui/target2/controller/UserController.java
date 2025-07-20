package com.JinRui.target2.controller;

import com.JinRui.target2.service.UserService;
import com.JinRui.target2.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName:UserController
 * Package: com.JinRui.target2.controller
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 17:26
 * @Version 1.0
 */
@RequestMapping("/login/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //通过userId获取用户信息
    @GetMapping("/getinfo/{userId}")
    public UserVo getInfo(@PathVariable Long userId){
        return userService.getInfoUerById(userId);
    }


    //通过userId更新用户信息，昵称
    @PutMapping("/updateinfo/{userId}")
    public String updateNickName(@PathVariable Long userId,@RequestParam String name){
        boolean is_success = userService.updateUserNickName(userId,name);

        if (is_success){
            return "修改成功";
        }
        return "修改失败";
    }

}

package com.JinRui.target2.service;

import com.JinRui.target2.entity.User;
import com.JinRui.target2.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * ClassName:UserService
 * Package: com.JinRui.target2.service
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 17:32
 * @Version 1.0
 */
@Service
public interface UserService extends IService<User> {

    //通过userId获取用户信息
    UserVo getInfoUerById(Long userId);

    //通过userId更新用户信息，昵称
    boolean updateUserNickName(Long userId,String name);
}

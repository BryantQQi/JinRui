package com.JinRui.target2.service.serviceimpl;

import com.JinRui.target2.entity.User;
import com.JinRui.target2.mapper.UserMapper;
import com.JinRui.target2.service.UserService;
import com.JinRui.target2.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * ClassName:UserServiceImpl
 * Package: com.JinRui.target2.service.serviceimpl
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 17:33
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    //通过userId获取用户信息
    @Override
    @Cacheable(key="'userId:' + #userId",value = "user:userVo")
    public UserVo getInfoUerById(Long userId) {

        User user = baseMapper.selectById(userId);
        UserVo userVo = new UserVo();
        userVo.setNick_name(user.getNickName());
        userVo.setSex(user.getSex());
        userVo.setPhone(user.getPhone());

        return userVo;
    }



    //通过userId更新用户信息，昵称
    @Override
    @CacheEvict(key="'userId:' + #userId",value = "user:userVo")
    public boolean updateUserNickName(Long userId,String name) {
        boolean is_success = false;
        if(!is_success){
            User user = new User();
            user.setNickName(name);
            user.setId(userId);
            baseMapper.updateById(user);
            is_success = true;
        }

        return is_success;
    }
}

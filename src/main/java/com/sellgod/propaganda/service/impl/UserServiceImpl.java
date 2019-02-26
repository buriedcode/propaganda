package com.sellgod.propaganda.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.sellgod.propaganda.dao.UserDao;
import com.sellgod.propaganda.entity.UserEntity;
import com.sellgod.propaganda.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 姜文超
 * @date 2018/11/10 11:42
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity>   implements UserService {


}

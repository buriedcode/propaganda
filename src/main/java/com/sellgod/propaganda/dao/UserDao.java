package com.sellgod.propaganda.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sellgod.propaganda.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}

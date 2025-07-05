package io.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.boot.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


}
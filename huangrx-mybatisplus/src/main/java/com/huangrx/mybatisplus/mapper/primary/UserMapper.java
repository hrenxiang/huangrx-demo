package com.huangrx.mybatisplus.mapper.primary;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huangrx.mybatisplus.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义接口使用条件构造器 第一种方式
     * @param wrapper 构造器
     * @return
     */
    List<User> selectAll1(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 自定义接口使用条件构造器 第二种方式
     * @param wrapper 构造器
     * @return
     */
    @Select("select * from `user` ${ew.customSqlSegment}")
    List<User> selectAll2(@Param(Constants.WRAPPER) Wrapper wrapper);
}

package com.huangrx.mybatisplus.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.huangrx.mybatisplus.model.entity.Dept;
import com.huangrx.mybatisplus.mapper.follow.DeptMapper;
import com.huangrx.mybatisplus.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@Service
@DS("db2")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}

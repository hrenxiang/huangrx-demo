package com.huangrx.evenlistener.translation.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author hrenxiang
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过id修改姓名
     * @param empId
     * @param empName
     */
    @Override
    public void updateEmpNameById(Integer empId, String empName) {
        String  sql = "update emp set ename=? where empno=?";
        jdbcTemplate.update(sql,empName,empId);
    }

    @Override
    public void updateEmpSalaryById(Integer empId, Double salary){

        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String  sql = "update emp set sal=? where empno=?";
        jdbcTemplate.update(sql,salary,empId);
    }

    @Override
    public String selectEmpNameById(Integer empId) {
        String sql = "select ename from emp where empno=?";
        String s = jdbcTemplate.queryForObject(sql, String.class, empId);
        return s;
    }
}
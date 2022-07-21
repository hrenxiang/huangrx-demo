package com.huangrx.evenlistener.translation.service;

import com.huangrx.evenlistener.translation.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 数据库访问层接口
     */
    @Autowired
    private UserDao userDao;

    /**
     * 通过id改姓名
     *
     * @param empId
     * @param empName
     */
    @Override
    public void updateEmpNameById(Integer empId, String empName) {
        userDao.updateEmpNameById(empId, empName);
        System.out.println(123);
    }

    /**
     * 通过id改薪水
     *
     * @param empId
     * @param salary
     */
    @Override
    //@Transactional
    public void updateEmpSalaryById(Integer empId, Double salary) {
        userDao.updateEmpSalaryById(empId, salary);
    }

    /**
     * 通过id找姓名
     *
     * @param empId
     * @return
     */
    @Override
    @Transactional
    public String selectEmpNameById(Integer empId) {
        return userDao.selectEmpNameById(empId);
    }

    /**
     * 为了便于核对数据库操作结果，不要修改同一条记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTwice(
            // 修改员工姓名的一组参数
            Integer empId, String newName,
            // 修改员工工资的一组参数
            Integer empIdSalary, Double newSalary
    ) {
        // 为了测试事务是否生效，执行两个数据库操作，看它们是否会在某一个失败时一起回滚
        userDao.updateEmpNameById(empId, newName);

        userDao.updateEmpSalaryById(empIdSalary, newSalary);

    }
}
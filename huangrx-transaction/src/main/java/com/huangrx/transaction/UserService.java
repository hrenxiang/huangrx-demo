package com.huangrx.transaction;

public interface UserService {

    /**
     * 通过id 更新员工名称
     *
     * @param empId
     * @param empName
     */
    public void updateEmpNameById(Integer empId, String empName);

    /**
     * 通过id修改员工薪水
     *
     * @param empId
     * @param salary
     */
    public void updateEmpSalaryById(Integer empId, Double salary);

    /**
     * 通过id查找员工姓名
     *
     * @param empId
     * @return
     */
    public String selectEmpNameById(Integer empId);

    /**
     * 为了测试事务是否生效，执行两个数据库操作，看它们是否会在某一个失败时一起回滚
     * @param empId
     * @param newName
     * @param empIdSalary
     * @param newSalary
     */
    public void updateTwice(// 修改员工姓名的一组参数
                            Integer empId, String newName,
                            // 修改员工工资的一组参数
                            Integer empIdSalary, Double newSalary);
}
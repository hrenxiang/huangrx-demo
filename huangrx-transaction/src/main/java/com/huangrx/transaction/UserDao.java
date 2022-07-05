package com.huangrx.transaction;

public interface UserDao {

    /**
     * 通过id 更新员工名称
     * @param empId
     * @param empName
     */
    public void updateEmpNameById(Integer empId, String empName);

    /**
     * 通过id修改员工薪水
     * @param empId
     * @param salary
     */
    public void updateEmpSalaryById(Integer empId, Double salary);

    /**
     * 通过id查找员工姓名
     * @param empId
     * @return
     */
    public String selectEmpNameById(Integer empId);
}
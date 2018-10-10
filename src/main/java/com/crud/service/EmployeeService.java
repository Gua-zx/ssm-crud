package com.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.bean.Employee;
import com.crud.bean.EmployeeExample;
import com.crud.bean.EmployeeExample.Criteria;
import com.crud.dao.EmployeeMapper;
@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;
	/**
	 * 查询所有员工
	 * @return
	 */
	public List<Employee> getAll() {
		// TODO Auto-generated method stub
		return employeeMapper.selectByExampleWithDept(null);
	}
	/**
	 * 保存员工信息
	 * @param employee
	 */
	public void saveEmp(Employee employee) {
		// TODO Auto-generated method stub
		employeeMapper.insertSelective(employee);
	}
	/**
	 * 校验员工姓名是否重复
	 * @param empName
	 * @return true 表示姓名不重复，可用
	 */
	public Boolean checkUser(String empName) {
		// TODO Auto-generated method stub
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		return count==0;
	}
	/**
	 * 根据员工ID查询
	 * @param id
	 * @return
	 */
	public Employee getEmp(Integer id) {
		// TODO Auto-generated method stub
		Employee employee = employeeMapper.selectByPrimaryKeyWithDept(id);
		return employee;
	}
	/**
	 * 员工更新
	 * @param employee
	 */
	public void updateEmp(Employee employee) {
		// TODO Auto-generated method stub
		employeeMapper.updateByPrimaryKeySelective(employee);
	}
	/**
	 * 删除员工
	 * @param id
	 */
	public void deleteEmp(Integer id) {
		// TODO Auto-generated method stub
		employeeMapper.deleteByPrimaryKey(id);
		
	}
	/**
	 * 删除员工二合一改进版
	 * @param ids
	 */
	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		//delete from xxx where emp_id in(1,2,3)
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);
		
		
	}

}

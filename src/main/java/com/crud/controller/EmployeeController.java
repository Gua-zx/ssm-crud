package com.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crud.bean.Employee;
import com.crud.bean.Msg;
import com.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 处理员工CRUD请求
 * @author hasee
 *
 */
@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	/**
	 * 员工删除(改进，支持单个删除与批量删除)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="emp/{ids}",method=RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("ids")String ids){
		if(ids.contains("-")){
			//批量删除
			String[] str_ids = ids.split("-");
			//组装id集合
			List<Integer> del_ids = new ArrayList<>();
			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else{
			//单个删除
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
/*	@ResponseBody
	@RequestMapping(value="emp/{id}",method=RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("id")Integer id){
		employeeService.deleteEmp(id);
		return Msg.success();
	}*/
	/**
	 * 更新员工数据
	 * 
	 * 如果直接发送ajax=PUT形式的请求
	 * Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
	 * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
	 * 1、配置上HttpPutFormContentFilter；
	 * 2、他的作用；将请求体中的数据解析包装成一个map。
	 * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
	 * 员工更新方法
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	public Msg saveEmp(Employee employee,HttpServletRequest request){
		System.out.println("请求体中的值："+request.getParameter("gender"));
		System.out.println("将要更新的员工数据："+employee);
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * 根据员工ID查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id){
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
		
	}
	
	/**
	 * 校验员工姓名
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkuser")
	public Msg checkuser(@RequestParam("empName")String empName){
		//先判断用户是否符合正则表达式
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if(!empName.matches(regx)){
			return Msg.fail().add("va_msg", "用户名必须是6-16位数字或字母的组成");
		}
		Boolean b = employeeService.checkUser(empName);
		if(b){
			return Msg.success();
		}else{
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}
	/**
	 * 员工保存
	 * @return
	 * 使用JSR303校验，需要导入Hibernate-Validator
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg savaEmp(@Valid Employee employee,BindingResult result){
		if(result.hasErrors()){
			//校验失败
			Map<String,Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名:"+fieldError.getField());
				System.out.println("错误信息:"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else{
		employeeService.saveEmp(employee);
		return Msg.success();
		}
	}
	/**
	 * 查询员工数据（分页查询）
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn){
		//引入PageHelper分页插件
		//在查询之前调用，传入页码，以及每页的大小
		PageHelper.startPage(pn, 5);
		//startPage后面的这个查询就是分页查询
		List <Employee> emps = employeeService.getAll();
		//使用PageInfo包装查询后的结果（包括页数等）
		//将PageInfo交给页面	
		PageInfo page = new PageInfo(emps,5);
		return Msg.success().add("pageInfo", page);
	}
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model){
		//这不是一个分页查询
		//引入PageHelper分页插件
		//在查询之前调用，传入页码，以及每页的大小
		PageHelper.startPage(pn, 5);
		//startPage后面的这个查询就是分页查询
		List <Employee> emps = employeeService.getAll();
		//使用PageInfo包装查询后的结果（包括页数等）
		//将PageInfo交给页面	
		PageInfo page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		return "list";
	}
}

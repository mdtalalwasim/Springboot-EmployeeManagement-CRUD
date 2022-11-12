package com.wasim.springboot.controller;

import java.security.Provider.Service;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.wasim.springboot.entity.Employee;
import com.wasim.springboot.service.EmpService;

@Controller
public class EmpController {

	@Autowired
	EmpService empService;

	@GetMapping("/")
	public String home(Model m) {

		List<Employee> emp = empService.getAllEmp();
		m.addAttribute("emp", emp);
		return "index.html";
	}

	//addEmployee
	@GetMapping("/addEmployee")
	public String addEmp() {

		return "addEmployee.html";
	}
	
	//edit
	@GetMapping("/editEmployee/{empId}")
	public String editEmp(@PathVariable int empId, Model m) {
		
		Employee e = empService.getEmpById(empId);
		m.addAttribute("employee",e);
		
		return "edit.html";
	}
	
	

	//
	@PostMapping("/registerEmployee")
	public String empRegister(@ModelAttribute Employee emp, HttpSession session) {

		empService.addEmp(emp);
		// System.out.println(emp);
		session.setAttribute("msg", "Employee Added Successfully...");
		return "redirect:/";
	}
	
	//update employee
	@PostMapping("/updateEmployee")
	public String updateEmp(@ModelAttribute Employee e, HttpSession session) {
		empService.addEmp(e);
		session.setAttribute("msg","Employee Updated Successfully.");
		return "redirect:/";
	}
	
	//delete employee
	@GetMapping("/deleteEmployee/{empId}")
	public String deleteEmp(@PathVariable int empId, HttpSession session) {
		
		empService.deleteEmpById(empId);
		session.setAttribute("msg", "Employee Deleted Successfully.");
		return "redirect:/";
		
	}

}

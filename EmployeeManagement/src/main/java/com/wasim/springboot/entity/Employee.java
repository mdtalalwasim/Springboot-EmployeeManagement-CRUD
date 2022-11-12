package com.wasim.springboot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMP_SYSTEM")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int empId;
	private String empName;
	private String empEmail;
	private String empAddress;
	private String empPhone;
	private int empSalary;
	
	
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Employee(int empId, String empName, String empEmail, String empAddress, String empPhone, int empSalary) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empEmail = empEmail;
		this.empAddress = empAddress;
		this.empPhone = empPhone;
		this.empSalary = empSalary;
	}



	public int getEmpId() {
		return empId;
	}



	public void setEmpId(int empId) {
		this.empId = empId;
	}



	public String getEmpName() {
		return empName;
	}



	public void setEmpName(String empName) {
		this.empName = empName;
	}



	public String getEmpEmail() {
		return empEmail;
	}



	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}



	public String getEmpAddress() {
		return empAddress;
	}



	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}



	public String getEmpPhone() {
		return empPhone;
	}



	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}



	public int getEmpSalary() {
		return empSalary;
	}



	public void setEmpSalary(int empSalary) {
		this.empSalary = empSalary;
	}



	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empEmail=" + empEmail + ", empAddress="
				+ empAddress + ", empPhone=" + empPhone + ", empSalary=" + empSalary + "]";
	}
	
	
	
	
	
	
	
	
}

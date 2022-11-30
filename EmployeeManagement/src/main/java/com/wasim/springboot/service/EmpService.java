package com.wasim.springboot.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wasim.springboot.entity.Employee;
import com.wasim.springboot.repository.EmpRepo;

@Service
public class EmpService {
	
	
	@Autowired
	private EmpRepo repo;
	
	public void addEmp(Employee e) {
		repo.save(e); 
	}
	
	
	public List<Employee> getAllEmp(){
		return repo.findAll();
	}
	
	
	public Employee getEmpById(int id) {
		Optional<Employee> e = repo.findById(id);

		if (e.isPresent()) {
			return e.get();
		}
		return null;

	}
	
	public void deleteEmpById(int id) {
		repo.deleteById(id);
	}
	
	
	public Page<Employee> getEmpByPagination(int currentPage, int size){
		
		Pageable page = PageRequest.of(currentPage, size);
		return repo.findAll(page);
	}

}

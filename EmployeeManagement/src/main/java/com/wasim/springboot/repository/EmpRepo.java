package com.wasim.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wasim.springboot.entity.Employee;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Integer> {

}

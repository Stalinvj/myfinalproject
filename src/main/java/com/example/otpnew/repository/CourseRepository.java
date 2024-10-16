package com.example.otpnew.repository;

 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.Course;
import com.example.otpnew.modal.SignUp;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByNameContainingIgnoreCase(String keyword);

 

	 

	 

	 
	
 
}


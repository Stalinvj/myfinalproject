package com.example.otpnew.repository;

 

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.SignUp;
@Repository
public interface SignUpRepository extends JpaRepository<SignUp, Integer> {
 

	List<SignUp> findByEmail(String email);

	boolean existsByEmail(String email);

 

	 
}

package com.example.otpnew.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.RegisterForm;
 
@Repository
public interface RegisterRepository extends JpaRepository<RegisterForm, Integer> {

	RegisterForm findByVerificationcode(String verificationcode);
RegisterForm findByEmailid(String emailid);
 

	 
}
 


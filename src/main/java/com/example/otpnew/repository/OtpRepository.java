 package com.example.otpnew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.otpnew.modal.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
	public Otp findByEmail(String email);
	public void deleteByEmail(String email);
	
	
}

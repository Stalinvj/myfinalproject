package com.example.otpnew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.Country;

 

 
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

}
 



package com.example.otpnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.Country;
import com.example.otpnew.modal.State;

 

 
@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
	 public List<State>findByCountry(Country countryid);

}
 



package com.example.otpnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.City;
import com.example.otpnew.modal.State;

 
 
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	public List<City>findByState(State stateid);
 

}
 



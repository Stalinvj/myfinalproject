package com.example.otpnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.otpnew.modal.City;
import com.example.otpnew.modal.State;
import com.example.otpnew.repository.CityRepository;

 
@Service
 public class CityService{
	 @Autowired
	 private CityRepository cityrepo;
	 public List<City>getCity(State state){
		 return cityrepo.findByState(state);
	 }
	 
 }



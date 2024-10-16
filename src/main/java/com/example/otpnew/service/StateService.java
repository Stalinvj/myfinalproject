package com.example.otpnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.otpnew.modal.Country;
import com.example.otpnew.modal.State;
import com.example.otpnew.repository.StateRepository;

 
@Service
public class StateService{
	@Autowired
	private StateRepository staterepo;
	public List<State>getState(Country country){
		return staterepo.findByCountry(country);
	}
	public List<State>getAllStates(){
		return staterepo.findAll();
	}
	 
}
 
 



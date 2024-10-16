package com.example.otpnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.otpnew.modal.Country;
import com.example.otpnew.repository.CountryRepository;
 

@Service
 public class CountryService{
	 @Autowired
	 private CountryRepository countryrepo;
	 public List<Country>getCountry(){
		 return countryrepo.findAll();
	 }
 }



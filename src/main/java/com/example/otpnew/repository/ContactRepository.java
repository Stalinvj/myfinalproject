package com.example.otpnew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.otpnew.modal.ContactForm;
@Repository
public interface ContactRepository extends JpaRepository<ContactForm, Integer> {

}

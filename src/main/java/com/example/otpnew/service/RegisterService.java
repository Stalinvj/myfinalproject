  package com.example.otpnew.service;

 
 
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.otpnew.modal.ContactForm;
import com.example.otpnew.modal.RegisterForm;
import com.example.otpnew.repository.ContactRepository;
import com.example.otpnew.repository.RegisterRepository;

import jakarta.mail.MessagingException;
 
import jakarta.mail.internet.MimeMessage;

 
 @Service
 public class RegisterService {
		@Autowired
	private RegisterRepository emailrepo;
		@Autowired
		private JavaMailSender sender;
		@Autowired
		private ContactRepository contactrepo;
	 
		 
		
     public RegisterForm saveForm(RegisterForm form ) {
     return emailrepo.save(form);
     }
     private Map<String, RegisterForm> tempreg = new ConcurrentHashMap<>();
 	public void register(RegisterForm reg,String siteUrl) throws UnsupportedEncodingException, MessagingException {
 		String randomcode=RandomStringUtils.randomAlphanumeric(8);
 		reg.setVerificationcode(randomcode);
 		reg.setEnabled(false);
 	    tempreg.put(randomcode, reg);
 		sendVerificationEmail(reg,siteUrl);
 	}
 
 	public void sendVerificationEmail(RegisterForm reg, String siteUrl) throws UnsupportedEncodingException, MessagingException {
 	 String toaddr=reg.getEmailid();
 	 String fromaddr="stalindhas111@gmail.com";
 	 String sendername="Stalin Vijay P";
 	 String subject="Verify Registration";
 	 String url=siteUrl+"/verify?code="+reg.getVerificationcode();
 	 String message="Dear"+ reg.getFname()+" "+reg.getLname()+",<br><br>"
 			+ "<h4>Please click the link below to verify your registration:<h4> <br><br>"
 			 +"<a href=\""+url+"\" target=\"_blank\" >VERIFY</a>";
 	 MimeMessage msg=sender.createMimeMessage();
 	 MimeMessageHelper helper=new MimeMessageHelper(msg);
 	 helper.setFrom(fromaddr,sendername);
 	 helper.setTo(toaddr);
 	 helper.setSubject(subject);
 	 helper.setText(message,true);
 	 sender.send(msg);
 	 		 
 	}

 	public RegisterForm verifyEmail(String verificationCode) {
 		  RegisterForm reg = tempreg.get(verificationCode);
 	    if (reg == null || reg.isEnabled()) {
 	        return null; 
 	    } else {
 	        reg.setEnabled(true);
 	   	emailrepo.save(reg); 	   tempreg.remove(verificationCode); 
 	       return reg;  
 	    }
 	}

 
 	public RegisterForm findPhoto(int id) {
		return emailrepo.findById(id).orElse(null) ;
 		
 	}

 	//for contact form
 	public ContactForm contactsave(ContactForm contact) {
		return contactrepo.save(contact);
 		
 	}
 
 	
	public boolean emailExists(String email) {
	    return emailrepo.findByEmailid(email)!=null ;
	}

 

 
 	 
 }
  
 
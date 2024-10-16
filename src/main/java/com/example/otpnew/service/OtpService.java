 package com.example.otpnew.service;

 
import com.example.otpnew.modal.Course;
import com.example.otpnew.modal.Otp;
import com.example.otpnew.modal.SignUp;
import com.example.otpnew.repository.CourseRepository;
import com.example.otpnew.repository.OtpRepository;
import com.example.otpnew.repository.SignUpRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private CourseRepository courserepo;
    
    @Autowired
    private SignUpRepository signuprepo;

    private static final int expirytimemin = 2;
    @Transactional
    public void sendOtpEmail(String email) throws MessagingException {
        String otpCode = generateOtp();
        saveOtpToDatabase(email, otpCode);
        sendEmail(email, otpCode);
    }

  

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @Transactional
    private void saveOtpToDatabase(String email, String otpCode) {
    	otpRepository.deleteByEmail(email);//same email means delete old email with otp in db
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setTemppassword(otpCode);
        otp.setVerify(false); // first not verified 
        otp.setCreatedAt(LocalDateTime.now()); // current tym setting
        otpRepository.save(otp);
    }

    private void sendEmail(String email, String otpCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP code is: " + otpCode);

        mailSender.send(message);
    }
    @Transactional
    public boolean verifyOtp(String email, String temppassword) {
        Otp otp = otpRepository.findByEmail(email);
        if (otp != null && otp.getTemppassword().equals(temppassword)) {
            otp.setVerify(true);
            otpRepository.save(otp);
            return true;
        }
        return false;
    }
    
    public boolean permanentPwd(String email,String permanentpwd) {
     
    	if(!checkValidPwd(permanentpwd)) {
    	 return false;
     }
    	
    	Otp user=otpRepository.findByEmail(email);
    	if(user !=null && user.isVerify()) {
    		user.setTemppassword(permanentpwd);
    		 otpRepository.save(user);
    		 user.setVerify(false);
    		 return true;
    		}
    	else {
    		return false;  
    	}
    }
    
    public boolean checkValidPwd(String password) {
    	 final String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
         Pattern pattern=Pattern.compile(regex);
       //Pattern object- match strings against the regex. 
         return pattern.matcher(password).matches();
    }
    
    
    //change existing pwd to new pwd
    public boolean newPermanentPwd(String email,String newPassword,String existingpwd) {
    	if(!checkValidPwd(newPassword)) {
    		return false;
    	}
    	Otp otp=otpRepository.findByEmail(email);
    	if(otp!=null && otp.getTemppassword().equals(existingpwd)) {
    		otp.setTemppassword(newPassword);
    		otpRepository.save(otp);
    		return true;
    	}
		return false;
		 
    	
    }
//   public void verifyNewPwd(String email,String newPassword) {
//	verifyPwd(email, newPassword);
//	   
//   }
    
    
    public boolean verifyPwd(String email,String permanentpwd) {
    	Otp otp=otpRepository.findByEmail(email);
    	if(otp!=null && otp.getTemppassword().equals(permanentpwd)) {
    		return true;
    	}
		return false;
    }
    
    public void forgotPwd(String email) throws MessagingException {
    	sendOtpEmail(email);
    }
     //for otp expiration
	public boolean otpExpired(Otp otp) {
        return LocalDateTime.now().isAfter(otp.getCreatedAt().plusMinutes(expirytimemin));
    }
	
	
	public List <Course> getCourse(String keyword){
		return courserepo.findByNameContainingIgnoreCase(keyword);
	}
	public Optional <Course> GetCourseById(int id){
		return courserepo.findById(id);
	}
	 
 
 


}

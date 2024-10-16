package com.example.otpnew.controller;

import com.example.otpnew.modal.City;
import com.example.otpnew.modal.ContactForm;
import com.example.otpnew.modal.Country;
import com.example.otpnew.modal.Course;
import com.example.otpnew.modal.Otp;
import com.example.otpnew.modal.RegisterForm;
import com.example.otpnew.modal.SignUp;
import com.example.otpnew.modal.State;
import com.example.otpnew.repository.CourseRepository;
import com.example.otpnew.repository.OtpRepository;
import com.example.otpnew.repository.RegisterRepository;
import com.example.otpnew.repository.SignUpRepository;
import com.example.otpnew.service.CityService;
import com.example.otpnew.service.CountryService;
import com.example.otpnew.service.OtpService;
import com.example.otpnew.service.RegisterService;
import com.example.otpnew.service.StateService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class OtpController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private OtpRepository otprepo;
     
    @Autowired
    private CourseRepository courserepo;
    
    @Autowired
    private SignUpRepository signuprepo;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RegisterRepository registerrepo;
    @Autowired
  private CountryService countryserv;

  @Autowired
  private StateService stateserv;

 @Autowired
 private CityService cityserv;
 
	@Autowired
	private JavaMailSender sender;
 
//@Value("${upload.dir}")
// private String upload;
	
   
 
 //for registration
 @GetMapping("/reg")
 public String showForm(Model model) {
     model.addAttribute("form", new RegisterForm());
     model.addAttribute("countrylist",countryserv.getCountry());
     return "verifyemail/register";
 }

 @PostMapping("/upload")
 public String handleForm(
 		@RequestParam("photom") MultipartFile photo,
         @Valid  @ModelAttribute("form") RegisterForm form,
         BindingResult bindingResult,  
         Model model,HttpServletRequest request) throws IOException {

	 if(registerService.emailExists(form.getEmailid())) {
		 bindingResult.rejectValue("emailid", "error.form", "Email already exists");
		 
	 }
	   LocalDate dob=form.getDob();
	   if(dob!=null) {
		   int age = Period.between(dob, LocalDate.now()).getYears();
		   if(age<18 || age >50) {
			   bindingResult.rejectValue("dob", "error.dob", "Age must be between 18 and 45 years.");
			   }
	   }

	   if (!form.getPhone().matches("^\\d{10}$")) {
           bindingResult.rejectValue("phone", "error.phone", "Mobile number must be exactly 10 digits.");
       }
	   
	   if (photo.isEmpty()) {
	        bindingResult.rejectValue("photom", "error.photom", "Photo is required. Photo must be jpg, jpeg and maximum size 5MB");
	    } else if (photo.getSize() > 5 * 1024 * 1024) {
	        bindingResult.rejectValue("photom", "error.photom", "Photo must not be greater than 5MB");
	    }
	   
     if (bindingResult.hasErrors()) {
      model.addAttribute("form", form); 
      model.addAttribute("countrylist", countryserv.getCountry());
      model.addAttribute("stateList", stateserv.getState(form.getCountry())); 
     if (form.getState() != null) {
          model.addAttribute("cityList", cityserv.getCity(form.getState()));  
      }
      
     return "verifyemail/register";
     }
     
   //using external location
//     String upload = "C:\\databaseimg";
//       String filename=photo.getOriginalFilename();
//     	 File savelocation=new File(upload,filename);
//     	 photo.transferTo(savelocation);
//     	 form.setPhoto("/databaseimg/" + filename); 
      
  
     try {
    	    
    	    String projectDir = System.getProperty("user.dir");
    	    String uploadDir = projectDir + "/imgnew";   
    	    String fileName = photo.getOriginalFilename();

    	    if (fileName != null && !fileName.isEmpty()) {
    	        File savelocation = new File(uploadDir);

    	        if (!savelocation.exists()) {
    	        	savelocation.mkdirs(); 
    	        }

    	        File newFile = new File(uploadDir, fileName);
    	        photo.transferTo(newFile); 
    	        form.setPhoto("/imgnew/"+ fileName );//in db we use
    	        
    	       
    	    } else {
    	        System.out.println("Invalid file name.");
    	    }
    	} catch (IOException e) {
    	    e.printStackTrace();  
    	}

    
     
     
     
 
 try {
		registerService.register(form,getSiteURL(request));
		 model.addAttribute("form", form);
		 return "verifyemail/success";
	} catch (UnsupportedEncodingException | MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		 model.addAttribute("error", "Failed to send verification email.");
      return "verifyemail/register";
	}
	 
	 }
 
 @GetMapping("/checkemail")
 @ResponseBody
 public ResponseEntity<String>checkemail(@RequestParam("email") String email){
	 boolean emailexist=registerService.emailExists(email);
	return ResponseEntity.ok(emailexist ? "Email already exists":"Email is available");
	 
 }

 
 private String getSiteURL(HttpServletRequest request) {
		// TODO Auto-generated method stub
 	String siteUrl=request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(),"");
	}

	@GetMapping("/state/{countryid}")
public @ResponseBody Iterable<State> getAllState(@PathVariable Country countryid) {
   return stateserv.getState(countryid);
}

@GetMapping("/city/{stateid}")
public @ResponseBody Iterable<City> getAllCity(@PathVariable State stateid) {
   return cityserv.getCity(stateid);
}

 

@RequestMapping("/verify")
public String verify(@RequestParam("code") String code, Model model) {
	//new change
    RegisterForm form = registerService.verifyEmail(code);
    if (form != null) {
        registerService.saveForm(form);  
        model.addAttribute("form", form);
        return "redirect:/otp-form";  
    } else {
        return "verifyemail/error";
    }
}
 //for otp sending
 
  @GetMapping("/otp-form")
    public String showOtpForm(Model model) {
        model.addAttribute("otpRequest", new Otp());
        return "send-otp-form";
    }

//  @PostMapping("/send-otp")
//  public String sendOtp(@ModelAttribute Otp otpRequest, Model model, HttpSession session) {
//	   
//      String email = otpRequest.getEmail();
//      System.out.println("Checking email: " + email);
//	  if (session.getAttribute("userId") != null) {
//          model.addAttribute("error", "Email already exists. Please login");
//          return "login"; 
//      }
//
//     if (registerService.emailExists(email)) {
//          model.addAttribute("error", "Email already registered. Please login.");
//          return "login";  
//      }
//
//      // Send the OTP
//      try {
//          otpService.sendOtpEmail(email);
//          model.addAttribute("message", "OTP sent to " + email + ". It will expire within 2 minutes.");
//      } catch (MessagingException e) {
//          model.addAttribute("error", "Failed to send OTP.");
//      }
//
//      model.addAttribute("email", email);  
//      return "verifyotp";  
//  }
  @PostMapping("/send-otp")
  public String sendOtp(@ModelAttribute Otp otpRequest, Model model, HttpSession session) {
      String email = otpRequest.getEmail();
      System.out.println("Checking email: " + email);

      
      String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
      if (!email.matches(emailRegex)) {
          model.addAttribute("error", "Invalid email format.");
          return "verifyemail/register";  
      }
      
      
     if (session.getAttribute("userId") != null) {
          model.addAttribute("error", "Email already exists. Please login");
          return "login"; 
      }
 
     RegisterForm form = registerrepo.findByEmailid(email);
      if (form == null) {
          model.addAttribute("error", "Please register first");
          model.addAttribute("form", new RegisterForm());
          model.addAttribute("countrylist", countryserv.getCountry());
          return "verifyemail/register";  
      }

     try {
          otpService.sendOtpEmail(email);
          model.addAttribute("message", "OTP sent to " + email + ". It will expire within 2 minutes.");
      } catch (MessagingException e) {
          model.addAttribute("error", "Failed to send OTP.");
      }

      model.addAttribute("email", email);  
      return "verifyotp";  
  }

    
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String temppassword, Model model, HttpServletRequest request) {
        Otp otp = otprepo.findByEmail(email);

        if (otp == null) {
            model.addAttribute("error", "OTP not found. Please request a new one.");
            model.addAttribute("email", email);  
            return "verifyotp";
        }

        if (otpService.otpExpired(otp)) {
            model.addAttribute("error", "OTP expired. Please request a new one.");
            model.addAttribute("email", email); 
            return "verifyotp";
        }

        if (otpService.verifyOtp(email, temppassword)) {
            HttpSession session = request.getSession();
            session.setAttribute("useremail", email);
            model.addAttribute("message", "OTP verified successfully.");
            return "newpassword";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            model.addAttribute("email", email);  
            return "verifyotp";
        }
    }

//OR
//        if (isOtpValid) {
//            model.addAttribute("message", "OTP verified successfully.");
//            return "newpassword";
//        } else {
//            model.addAttribute("email", email);
//            model.addAttribute("error", "Invalid OTP. Please try again.");
//            return "verifyotp";
//        }
//   }
//    
  
    @PostMapping("/new-password")
    public String newPassword(@RequestParam String temppassword,@RequestParam String confirmpwd,Model model,HttpServletRequest request) {
    	String email=(String) request.getSession().getAttribute("useremail");
    	if(email==null) {
    		model.addAttribute("error","Session expired. Please try again");
    		return "redirect:/send-otp-form";
    	}
    	
    	//new change
    	if(!temppassword.equals(confirmpwd)) {
    		model.addAttribute("error","Password do not match. Try Again");
    		return "newpassword";
    	}
    	boolean checkpwd=otpService.checkValidPwd(temppassword);
    	if(checkpwd) {
    		boolean newpass=otpService.permanentPwd(email, temppassword);
    	 
    	 
    	if(newpass) {
    	model.addAttribute("message","Password updated successfully");
    	
    	 return "login";
    	}
    	else {
            model.addAttribute("error", "An error occurred while updating the password. Please try again.");
            return "newpassword";  
        }
    	}
    	else {
		model.addAttribute("error","Password must be atleast 8 characters long including letters,"
				+ "specialcharacters and numbers.");
		return "newpassword";
    	}
	}
    
    //for login 
    @PostMapping("/login")
    public String loginPwd(@RequestParam String emailid,@RequestParam String pass,Model model,HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String loggedin) {
    	 
    	 RegisterForm form = registerrepo.findByEmailid(emailid);
    	   
    		    
    		    if (form == null) {
    		        model.addAttribute("error", "Please register first");
    		        model.addAttribute("form", new RegisterForm());
    		        model.addAttribute("countrylist",countryserv.getCountry());
    		        return "verifyemail/register";
    		    }
    	   
    	Otp otp=otprepo.findByEmail(emailid);
    	if(otp!=null && otpService.verifyPwd(emailid, pass)) {
    		HttpSession session=request.getSession();
    	 session.setAttribute("userverify", emailid);
    	 
    	 session.setAttribute("userId", form.getId());
             
            if(request.getParameter("loggedin")!=null) {
    		Cookie ck=new Cookie("userck",emailid);
    		ck.setMaxAge(30 * 24 * 60 * 60);
    		response.addCookie(ck);
    		
    	}  
    	  return "redirect:/dash";
    	  
    }else {
    		model.addAttribute("error", " Invalid email or password");
    		return "login";
    	}
		 
		 
    }
    
 

    //for forgot pwd 
    @PostMapping("/forgotpwd")
    public String forgotPwd(@RequestParam String email,Model model)  {
    	try {
			otpService.sendOtpEmail(email);
			model.addAttribute("email",email);
			return"verifyotp";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("error", "Error sending OTP. Please try again.");
			return "forgotpass";
		}
    	
		}
    
   
    
 
    //change existing pwd and setting new pwd
    @PostMapping("/newpassnew")
    public String newPassNew(@RequestParam String exist,@RequestParam String newpass,Model model,
    		 HttpServletRequest request) {
    	String email=(String) request.getSession().getAttribute("userverify");
    	String userid=(String) request.getSession().getAttribute("userverify");
    	if(email==null || userid==null) { //new change
    		 
        		model.addAttribute("error","Session expired. Please login again");
        		return "login";
        	}
    	boolean existingpwd=otpService.verifyPwd(email, exist);
    	if(!existingpwd) {
    		model.addAttribute("error","Existing password is incorrect");
    		return"dashboard/changepwd";
    	}
    	
    		boolean newpwd=otpService.checkValidPwd(newpass);
    		if(!newpwd) {
    			model.addAttribute("error","New Password does not meet the criteria");
    			return "dashboard/dashboard";
    		}
    		boolean newpwdverify=otpService.newPermanentPwd(email, newpass,exist);
    		if(newpwdverify) {
    			model.addAttribute("success","Password Updated successfully");
    			return "login";
    		}
    		else {
    			model.addAttribute("error","Failed to update the password. Please try again");
    			return"dashboard/changepwd";
    		}
    		
    		
    	}
    
   //for courses getting from db
    
    @GetMapping("/search")
    public String searchCourses(@RequestParam(value = "keyword", required = false) String keyword, Model model, RedirectAttributes attri) {
        if (keyword == null || keyword.isEmpty()) {
            attri.addFlashAttribute("message", "Please enter a search keyword");
        } else {
            List<Course> courses = otpService.getCourse(keyword);
            if (courses.isEmpty()) {
                attri.addFlashAttribute("message", "No courses found");
            } else {
                attri.addFlashAttribute("courses", courses);
            }
        }  
        return "redirect:/dash";  
    }

    //to get the course using id from db ajax used in dashboard html page
    @GetMapping("/coursedetails/{id}")
    @ResponseBody
    public Course getCourseDetails(@PathVariable("id") int id) {
        return otpService.GetCourseById(id).orElse(null);
        
        
    }
    
    //when signup details saved to db one user can sign only one course
    @PostMapping("/signup")
    public String signUpCourse(@RequestParam("courseid") int courseId, HttpSession session,Model model
 ,RedirectAttributes attri   		   ) {
    	 String email=(String) session.getAttribute("userverify");
    	 if(email==null) {
    		 model.addAttribute("error","Session Expired.Please login again");
    		 return "login";
    	 }
		
    	Optional<Course>course= courserepo.findById(courseId);
        if (!course.isPresent()) {
            model.addAttribute("error", "Course not found.");
            return "dashboard/dashboard ";
        }
    	 
    	if (signuprepo.existsByEmail(email)) {
    	    attri.addFlashAttribute("error", "You have already signed up for the course");
    	    return "redirect:/dash";
    	}
    	 
    	SignUp sign=new SignUp();
    	sign.setCourseId(courseId);
    	sign.setEmail(email);
     
    	try {
    	signuprepo.save(sign);
    	model.addAttribute("message","Signup successful");
    	 
    	return"dashboard/dashboard";
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		model.addAttribute("error", "Failed to Sign up. Please try again");
            return "dashboard/dashboard";
    	}
    }
   
     
   
    @GetMapping("/picked")
    public String showUserCourses(HttpSession session, Model model,RedirectAttributes attri) {
        String email = (String) session.getAttribute("userverify");
        if (email == null) {
            model.addAttribute("error", "Session Expired. Please login again.");
            return "login";  
        }
        
        List<SignUp> signu = signuprepo.findByEmail(email);
        if (signu.isEmpty()) {
            attri.addFlashAttribute("error", "No courses found in your account.");
            return "redirect:/dash";
        }

        SignUp signup = signu.get(0);

     Optional<Course> course = courserepo.findById(signup.getCourseId());
        
        Course courses = course.get();
        model.addAttribute("coursename", courses.getName());
        model.addAttribute("courseduration", courses.getDuration());
        model.addAttribute("coursefee", courses.getFees());
       
      return "dashboard/pickedcourse";  
    }

 
 

//for dashboard taking id from session to update user based on id in profile
@GetMapping("/dash")
public String dash(Model model, HttpSession session, @ModelAttribute("message") String message,@ModelAttribute("error") String error) {
	String email= (String) session.getAttribute("userverify");
    Integer userId = (Integer) session.getAttribute("userId");
    if(email!=null && userId!=null) {
    	  RegisterForm form=registerrepo.findByEmailid(email);
    	if(form!=null && form.getEmailid().equals(email)) {
    		  model.addAttribute("userid", userId);
    		  
    		   if (message != null && !message.isEmpty()) {
                   model.addAttribute("message", message);
               }
              
    		   if (error != null && !error.isEmpty()) {
                   model.addAttribute("error", error);
               }
    	         @SuppressWarnings("unchecked")
				List<Course> courses = (List<Course>) model.asMap().get("courses");
    		   if (courses != null) {
                   model.addAttribute("courses", courses);
               }
    		  
    		   return "dashboard/dashboard"; 
    	}
    	else {
    		model.addAttribute("error","check your registered EmailId and  Please log in again");
    		return "login";
    	}
    	
    }else {
    	model.addAttribute("error","session expired. please login again");
    	   return "login";
    }
  
  
}


//get the profile details from database
@GetMapping("/profile/{id}")
public String profileUpdate( @PathVariable("id") int id,Model model,HttpSession session)
  {
	   
	String email=(String) session.getAttribute("userverify");
	if(email==null) {
		model.addAttribute("error","session expired.Please login again");
		return "login";
	}
	
	RegisterForm form=registerService.findPhoto(id); 
	if(form!=null) {
		model.addAttribute("pic", form.getPhoto());
		model.addAttribute("fname",form.getFname());
		model.addAttribute("lname",form.getLname());
		model.addAttribute("phone",form.getPhone());
		model.addAttribute("emailid",form.getEmailid());
		model.addAttribute("dob",form.getDob());
		model.addAttribute("address",form.getAddress());
		model.addAttribute("userid",id);
		System.out.println("Profile ID received: " + id);


	}
	else {
		model.addAttribute("error","user not found");
	}
	return "dashboard/profile";
}

 

//update the profile details to database
@PostMapping("/profile/{id}")
public String updateProfile(@PathVariable("id") int id   ,  @RequestParam String fname,
@RequestParam String lname,@RequestParam String address,  @RequestParam(required = false) MultipartFile photo,   Model model, HttpSession session
 ) {
    String email = (String) session.getAttribute("userverify");
    Integer userId = (Integer) session.getAttribute("userId");

    
    if (email == null || userId == null) {
        model.addAttribute("error", "Session expired. Please log in again.");
        return "login"; 
    }
RegisterForm form=registerrepo.findById(userId).orElse(null);
if(form==null) {
	model.addAttribute("error","User not found Please login again");
	return "dashboard/profile";
}

if (!isValidName(fname)) {
    model.addAttribute("error", "First name must be between 3 and 15 characters long and cannot contain numbers or special characters.");
    setModelAttributes(model, form);  
    return "dashboard/profile";
}

if (!isValidNames(lname)) {
    model.addAttribute("error", "Last name must be at least 1 character long and cannot contain numbers or special characters.");
    setModelAttributes(model, form);  
    return "dashboard/profile";
}
if (!isValidAddress(address)) {
    model.addAttribute("error", "Address must be between 2 and 100 characters and can only contain letters, numbers, spaces, commas, slashes, and hyphens.");
    setModelAttributes(model, form);  
    return "dashboard/profile";
}
if (photo.isEmpty()) {
    model.addAttribute("error", "Photo is required. It must be jpg, jpeg, and maximum size 5MB.");
    setModelAttributes(model,form);
    return "dashboard/profile";
} else if (photo.getSize() > 5 * 1024 * 1024) {
    model.addAttribute("error", "Photo must not be greater than 5MB.");
    setModelAttributes(model, form);
    return "dashboard/profile";
} else if (!photo.getOriginalFilename().toLowerCase().matches(".*\\.(jpg|jpeg)$")) {
    model.addAttribute("error", "Photo must be a jpg or jpeg file.");
    setModelAttributes(model, form);
    return "dashboard/profile";
}

form.setFname(fname);
form.setLname(lname);
form.setAddress(address);

if(photo!=null && !photo.isEmpty()) {
	String filename=photo.getOriginalFilename();
	if(filename!=null && !filename.isEmpty()) {
		String uploadpath=System.getProperty("user.dir");
		String savepath=uploadpath+"/imgnew"  ;
		File savelocationcreate=new File(savepath);
		if(!savelocationcreate.exists()) {
			savelocationcreate.mkdirs();
		}
		File newone=new File(savepath,filename);
		try {
			photo.transferTo(newone);
			form.setPhoto("/imgnew/"+filename);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 model.addAttribute("error", "Error uploading file.");
             return "dashboard/profile";  
		}
	}
}
registerService.saveForm(form);
model.addAttribute("message","Profile updated successfully");
return "redirect:/dash";
  }
 

private boolean isValidName(String fname) {
  String namePattern = "^[A-Za-zà-ÿÀ-ÿ]{3,15}( [A-Za-zà-ÿÀ-ÿ]{3,15})*$";
    
    return fname != null && fname.matches(namePattern);
}



private boolean isValidNames(String lname) {
    String namePattern = "^[A-Za-zà-ÿÀ-ÿ]{1,15}( [A-Za-zà-ÿÀ-ÿ]{1,15})*$";
    
    return lname != null && lname.matches(namePattern);
}


private boolean isValidAddress(String address) {
     
    String addressPattern = "^[A-Za-z0-9\\s,/-]{2,100}$";
    
    return address != null && address.matches(addressPattern);
}
 
private void setModelAttributes(Model model, RegisterForm form) {
 model.addAttribute("pic", form.getPhoto());
 model.addAttribute("fname", form.getFname());
 model.addAttribute("lname", form.getLname());
 model.addAttribute("phone", form.getPhone());
 model.addAttribute("emailid", form.getEmailid());
 model.addAttribute("dob", form.getDob());
 model.addAttribute("address", form.getAddress());
 model.addAttribute("userid", form.getId());
}

@GetMapping("/logout")
public String logoutAll(HttpServletRequest req,HttpServletResponse res,HttpSession session) {
	Cookie ck=new Cookie("userck",null);
	ck.setMaxAge(0);
	res.addCookie(ck);
	req.getSession().invalidate();
	return "login";
	
}

@GetMapping("/contact")
public String showContact(Model model) {
	model.addAttribute("contact", new ContactForm());
	return "contactform";
}


@PostMapping("/contactf")
public String contactForm(@Valid @ModelAttribute("contact") ContactForm contact,BindingResult bindingres,Model model) {
	
	if(bindingres.hasErrors()) {
		return "contactform";
	}
	  
	 String to="stalindhas111@gmail.com";
	 SimpleMailMessage message=new SimpleMailMessage();
	 message.setFrom(contact.getEmail());
	 message.setTo(to);  
	 message.setSubject(contact.getSub());
	 message.setText(contact.getMsg());
 
	 try {
	        sender.send(message);
	        model.addAttribute("message", "Your response has been sent to " + to);
	    } catch (Exception e) {
	        
	        e.printStackTrace();
	        model.addAttribute("error", "Unable to send email. Please try again later.");
	    }
 
	registerService.contactsave(contact);
	 
	 return "contactform";
}


@GetMapping("/nav")
public String nav() {
	return "homenavbar";
}
@GetMapping("/nav2")
public String nav2() {
 
	
	return "navbar";
}
@GetMapping("/pwd")
public String changepwd() {
	return"dashboard/changepwd";
}

@GetMapping("/picking")
public String picking() {
	return"dashboard/pickedcourse";
}
@GetMapping("/profile")
public String profiles() {
	return"dashboard/profile";
}
@GetMapping("/otp")
public String showOtpForm1(Model model) {
    model.addAttribute("otp", new Otp());
    return "login";
}
@GetMapping("/forgot")
public String newone() {
	return "forgotpass";
	
}

@GetMapping("/newpass")
public String newpass() {
	return "newpassword";
	
}
@GetMapping("/verifyotp")
public String verify() {
	return "verifyotp";
	
}
 
@GetMapping("/suc")
public String success() {
	return "verifyemail/success";
}

@GetMapping("/err")
public String error() {
	return "verifyemail/error";
}

@GetMapping("/foot")
public String footer() {
	return "footer";
}
 
@GetMapping("/home")
public String home(Model model) {
    model.addAttribute("page", "home");
    return "home"; // return your Thymeleaf template
}



    }
     
    
    
    
 
 
   
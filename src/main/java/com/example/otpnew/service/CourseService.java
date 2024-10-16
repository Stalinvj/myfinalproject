//package com.example.otpnew.service;
//
// import java.util.List;
//import java.util.Optional;
// 
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.otpnew.modal.Course;
//import com.example.otpnew.repository.CourseRepository;
// 
// 
//@Service
//public class CourseService {
//@Autowired
//private CourseRepository courserepo;
//
//public List <Course> getCourse(String keyword){
//	return courserepo.findByNameContainingIgnoreCase(keyword);
//}
//public Optional <Course> GetCourseBtId(int id){
//	return courserepo.findById(id);
//}
// 
////public boolean signUp(int courseId) {
////	try {
////	Course course=new Course();
////	course.setCourseId(courseId);
////	courserepo.save(course);
////	return true;
////	}
////	catch(Exception e){
////		 e.printStackTrace();
////		return false;
////	}
////	
////}
//
//
//}

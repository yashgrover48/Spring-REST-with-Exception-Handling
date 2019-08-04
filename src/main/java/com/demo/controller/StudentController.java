package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.Student;
import com.demo.rest.StudentErrorResponse;
import com.demo.rest.StudentNotFoundException;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	private List<Student> theStudents;
	
	// define @PostConstruct to load the student data only once
	@PostConstruct
	public void loadData(){
		theStudents=new ArrayList<>();
		theStudents.add(new Student("Yash","Grover"));
		theStudents.add(new Student("Rahul","Kumar"));
		theStudents.add(new Student("Nimish","Tuteja"));
	}
	
	@GetMapping("/students")
	public List<Student> getStudents(){
		return theStudents;
	}
	
	// define an end point for /students/{studentId}
	@GetMapping("/students/{studentId}")
	public Student getStudent(@PathVariable int studentId){
		
		// just index it into the list.. keep it simple for now
		
		// check the studentId against list size
		if((studentId>=theStudents.size())||(studentId<0)){
			throw new StudentNotFoundException("Student id not found - "+studentId);
		}
		return theStudents.get(studentId);
	}
	
	// Add an exception handler using @ExceptionHandler
	
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc){
		
		// create a StudentErrorResponse
		
		StudentErrorResponse error= new StudentErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEntity
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	// Add another exeption handler for any other exception
	
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(Exception exc){ 
		// create a StudentErrorResponse
		
		StudentErrorResponse error= new StudentErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
				
		// return ResponseEntity
				
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	
	}
}
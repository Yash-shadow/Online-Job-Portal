package com.controller;


import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.exception.CandidateNotFoundException;
import com.exception.CandidateValidationExceptioncheck;
import com.exception.FormatException;
import com.exception.ProjectNotFoundException;
import com.exception.feedbackException;
import com.exception.skillNotFoundException;
import com.model.ExceptionResponse;
import com.exception.NoSuchJobFoundException;
import com.exception.NotShortlistedException;
import com.exception.NullValueException;



@ControllerAdvice
public class ControllerAdvices extends ResponseEntityExceptionHandler {
    

    @ExceptionHandler({CandidateNotFoundException.class})
    public ResponseEntity<Object> handleItemNotFoundException(CandidateNotFoundException c,WebRequest req)
    {
        
        return  new ResponseEntity<>(c.toString(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidateValidationExceptioncheck.class)
    public ResponseEntity<Object> handleValdationException(CandidateValidationExceptioncheck v,WebRequest req){
        
         return  new ResponseEntity<>(v.toString(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FormatException.class)
    public ResponseEntity<Object> handleFormatException(FormatException v,WebRequest req){
        
         return  new ResponseEntity<>(v.toString(),HttpStatus.FORBIDDEN);
    }
   
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException v,WebRequest req){
        
        return  new ResponseEntity<>(v.toString(),HttpStatus.FORBIDDEN);
   }
    @ExceptionHandler(skillNotFoundException.class)
    public ResponseEntity<Object> handleSkillNotFoundException(skillNotFoundException v,WebRequest req){
        
        return  new ResponseEntity<>(v.toString(),HttpStatus.FORBIDDEN);
   }
    @ExceptionHandler(feedbackException.class)
    public ResponseEntity<Object> handlefeedbackException(feedbackException v,WebRequest req){
        
        return  new ResponseEntity<>(v.toString(),HttpStatus.FORBIDDEN);
   }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleCandidateAuthenticationException(BadCredentialsException v,WebRequest req){
        
    	ExceptionResponse excpResp = new ExceptionResponse(false, v.getMessage(), String.valueOf(HttpStatus.NOT_FOUND));
    	
        return  new ResponseEntity<>(excpResp,HttpStatus.NOT_FOUND);
   }
    
    @ExceptionHandler(NoSuchJobFoundException.class)
    public ResponseEntity<Object> handleNoSuchJobFoundException(NoSuchJobFoundException e, WebRequest req){
    	return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(NotShortlistedException.class)
    public ResponseEntity<Object> handleNotShortlistedException(NotShortlistedException e, WebRequest req){
    	return new ResponseEntity<>(e.toString(), HttpStatus.NOT_ACCEPTABLE);
    }
    
    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<Object> handleNullValueException(NullValueException e, WebRequest req){
    	return new ResponseEntity<>(e.toString(), HttpStatus.NOT_ACCEPTABLE);
    }
}

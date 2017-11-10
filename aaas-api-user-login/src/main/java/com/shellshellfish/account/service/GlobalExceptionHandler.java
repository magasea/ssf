package com.shellshellfish.account.service;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shellshellfish.account.exception.UserException;
import com.shellshellfish.account.model.Error;

@ControllerAdvice
public class GlobalExceptionHandler{
	
	//@ExceptionHandler(MissingServletRequestParameterException.class)
	//@ExceptionHandler(RangeValidationException.class)
	//@ExceptionHandler(ServletRequestBindingException.class)
		
	public GlobalExceptionHandler(){;}
	
	//设置此handler处理所有异常
    @ExceptionHandler(value=Exception.class)
    public void defaultErrorHandler(){
        System.out.println("-------------default error");
    }
    
    
    
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Error[]> RangeErrorHandler(ConstraintViolationException e){
	    //String userId = e.getUserId();
	    //e.getConstraintViolations().
		Error[] error=null;
		Set<ConstraintViolation<?>> cons=e.getConstraintViolations();
		if (cons!=null) {
			ConstraintViolation<?>[] con=getconstraint(cons);
			error=new Error[con.length];
			for (int i=0;i<con.length;i++) {
			  String errmsg =con[i].getMessage();
			
			
			  /*
			  Path pname=con[i].getPropertyPath();
			  Iterator<Node> its=pname.iterator();			
			  String paraname= getParameterName(its);
			  */
			  error[i] = new Error(errmsg);
			  error[i].setCode(i);
			}
		}else {
			error =new Error[1];
			error[0]=new Error(e.getMessage());
		}
		return new ResponseEntity<Error[]>(error,HttpStatus.BAD_REQUEST);
   }
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<Error> UserExceptionHandler(UserException e) {
		Error error = new Error(e.getMsg());
		error.setCode(Integer.parseInt(e.getCode()));
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}

	
   public ConstraintViolation<?>[] getconstraint(Set<ConstraintViolation<?>> cons) {
		  
		String msg=null;
		Iterator<ConstraintViolation<?>> it = cons.iterator();
		int len= cons.size();
		ConstraintViolation<?> con[]=new ConstraintViolation<?>[len];
		int i=0;
		while (it.hasNext()) {  
		   con[i++] = it.next();
		  //System.out.println(str);  
		}  	
		return con;
   }
   
   @ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Error> ParamMissingErrorHandler(MissingServletRequestParameterException e){
	    String errmsg=e.getMessage();
	    Error error = new Error(errmsg);
	    String pname=e.getParameterName();
	    String ptype= e.getParameterType();
	    return new ResponseEntity<Error>(error,HttpStatus.BAD_REQUEST);
	}
	
	
	
	public String getParameterName(Iterator<Node> its) {
		
		Node node;
		String name="";
		while (its.hasNext()) {  
			   node = its.next();
			   name=name+node.getName()+".";
			   //System.out.println(str);  
		}
		
		return name.substring(0,name.length()-1);
	}

	
}

package com.shellshellfish.aaas.userinfo.exception;

import java.util.Iterator;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.shellshellfish.aaas.userinfo.model.dto.ErrorDTO;


@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	public GlobalExceptionHandler() {
	}

	//设置此handler处理所有异常
	@ExceptionHandler(value = Exception.class)
	public void defaultErrorHandler(Exception e) {
		logger.error(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDTO> RangeErrorHandler(MethodArgumentNotValidException e) {
		ErrorDTO error = new ErrorDTO(e.getMessage());
		//error.setCode(404);
		return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDTO[]> RangeErrorHandler(ConstraintViolationException e) {
		//String userId = e.getUserId();
		//e.getConstraintViolations().
		ErrorDTO[] error = null;
		Set<ConstraintViolation<?>> cons = e.getConstraintViolations();
		if (cons != null) {
			ConstraintViolation<?>[] con = getconstraint(cons);
			error = new ErrorDTO[con.length];
			for (int i = 0; i < con.length; i++) {
				String errmsg = con[i].getMessage();
			
			
			  /*
			  Path pname=con[i].getPropertyPath();
			  Iterator<Node> its=pname.iterator();			
			  String paraname= getParameterName(its);
			  */
				error[i] = new ErrorDTO(errmsg);
				error[i].setCode(i);
			}
		} else {
			error = new ErrorDTO[1];
			error[0] = new ErrorDTO(e.getMessage());
		}
		return new ResponseEntity<ErrorDTO[]>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserInfoException.class)
	public ResponseEntity<ErrorDTO> UserInfoExceptionHandler(UserInfoException e) {
		ErrorDTO error = new ErrorDTO(e.getMsg());
		error.setCode(Integer.parseInt(e.getCode()));
		return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
	}


	public ConstraintViolation<?>[] getconstraint(Set<ConstraintViolation<?>> cons) {

		String msg = null;
		Iterator<ConstraintViolation<?>> it = cons.iterator();
		int len = cons.size();
		ConstraintViolation<?> con[] = new ConstraintViolation<?>[len];
		int i = 0;
		while (it.hasNext()) {
			con[i++] = it.next();
			//System.out.println(str);
		}
		return con;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorDTO> ParamMissingErrorHandler(MissingServletRequestParameterException e) {
		String errmsg = e.getMessage();
		ErrorDTO error = new ErrorDTO(errmsg);
		String pname = e.getParameterName();
		String ptype = e.getParameterType();
		return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
	}


	public String getParameterName(Iterator<Node> its) {

		Node node;
		String name = "";
		while (its.hasNext()) {
			node = its.next();
			name = name + node.getName() + ".";
			//System.out.println(str);
		}

		return name.substring(0, name.length() - 1);
	}


}

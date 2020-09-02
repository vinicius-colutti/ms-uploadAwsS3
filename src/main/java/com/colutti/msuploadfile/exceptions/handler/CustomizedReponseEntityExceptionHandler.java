package com.colutti.msuploadfile.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.colutti.msuploadfile.exceptions.ExceptionRespose;
import com.colutti.msuploadfile.exceptions.FileException;

@ControllerAdvice
@RestController
public class CustomizedReponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(FileException.class)
	public final ResponseEntity<ExceptionRespose> fileException(Exception ex, WebRequest request) {
		ExceptionRespose exceptionRespose = new ExceptionRespose(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionRespose, HttpStatus.BAD_REQUEST);
	}

}

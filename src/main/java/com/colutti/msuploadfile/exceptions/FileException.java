package com.colutti.msuploadfile.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileException(String exception) {
		super(exception);
	}

}

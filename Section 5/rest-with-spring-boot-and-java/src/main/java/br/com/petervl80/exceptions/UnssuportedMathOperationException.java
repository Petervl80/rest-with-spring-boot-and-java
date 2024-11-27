package br.com.petervl80.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnssuportedMathOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnssuportedMathOperationException(String ex) {
		super(ex);
	}

}
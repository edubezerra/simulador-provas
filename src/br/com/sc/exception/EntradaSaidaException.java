package br.com.sc.exception;

import java.io.IOException;

public class EntradaSaidaException extends RuntimeException {

	public EntradaSaidaException(IOException e) {
		super(e);
	}

	public EntradaSaidaException(Exception e) {
		super(e);
	}

	public EntradaSaidaException(String msg) {
		super(msg);
	}

}

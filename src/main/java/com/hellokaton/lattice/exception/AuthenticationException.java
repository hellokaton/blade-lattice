package com.hellokaton.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/5
 */
public class AuthenticationException extends AccountException {

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}

package com.hellokaton.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/5
 */
public class UnknownAccountException extends AccountException {

    public UnknownAccountException() {
    }

    public UnknownAccountException(String message) {
        super(message);
    }

    public UnknownAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownAccountException(Throwable cause) {
        super(cause);
    }

}

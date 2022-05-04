package com.hellokaton.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/5
 */
public class AccountException extends LatticeException {

    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountException(Throwable cause) {
        super(cause);
    }
}

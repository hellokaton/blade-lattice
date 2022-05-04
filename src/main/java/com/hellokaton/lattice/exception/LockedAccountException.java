package com.hellokaton.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/5
 */
public class LockedAccountException extends DisabledAccountException {

    public LockedAccountException() {
    }

    public LockedAccountException(String message) {
        super(message);
    }

    public LockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockedAccountException(Throwable cause) {
        super(cause);
    }

}

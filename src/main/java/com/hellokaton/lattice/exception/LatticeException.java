package com.hellokaton.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/4
 */
public class LatticeException extends RuntimeException {

    public LatticeException() {
    }

    public LatticeException(String message) {
        super(message);
    }

    public LatticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LatticeException(Throwable cause) {
        super(cause);
    }
}

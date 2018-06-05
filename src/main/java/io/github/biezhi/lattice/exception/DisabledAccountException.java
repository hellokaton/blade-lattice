package io.github.biezhi.lattice.exception;

/**
 * @author biezhi
 * @date 2018/6/5
 */
public class DisabledAccountException extends AccountException {

    public DisabledAccountException() {
    }

    public DisabledAccountException(String message) {
        super(message);
    }

    public DisabledAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisabledAccountException(Throwable cause) {
        super(cause);
    }
    
}

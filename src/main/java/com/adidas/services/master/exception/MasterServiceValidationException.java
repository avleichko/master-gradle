package com.adidas.services.master.exception;

public class MasterServiceValidationException extends RuntimeException {
    public MasterServiceValidationException() {
        super();
    }

    public MasterServiceValidationException(String s) {
        super(s);
    }

    public MasterServiceValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MasterServiceValidationException(Throwable throwable) {
        super(throwable);
    }
}

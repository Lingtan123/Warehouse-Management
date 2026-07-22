package com.wms.exception;

import lombok.Getter;

@Getter
public class myException extends RuntimeException {
    private final AllException exception;

    public myException(AllException exception) {
        super(exception.getError());
        this.exception = exception;
    }
}

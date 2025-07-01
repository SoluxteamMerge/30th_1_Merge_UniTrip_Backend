package com.Solux.UniTrip.common.exception;
//해당 객체를 찾을 수 없을 때
//백다현

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

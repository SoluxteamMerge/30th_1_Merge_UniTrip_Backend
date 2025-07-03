package com.Solux.UniTrip.common.exception;
//요청값이 올바르지 않을 때
//백다현

public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
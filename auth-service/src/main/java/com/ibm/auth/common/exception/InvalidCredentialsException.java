package com.ibm.auth.common.exception;

public class InvalidCredentialsException extends RuntimeException{

   public InvalidCredentialsException(String message){
        super(message);
    }
}

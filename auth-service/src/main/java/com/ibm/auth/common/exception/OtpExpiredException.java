package com.ibm.auth.common.exception;

public class OtpExpiredException extends RuntimeException{

   public OtpExpiredException(String message){
        super(message);
    }
}

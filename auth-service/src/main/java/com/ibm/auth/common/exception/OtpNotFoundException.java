package com.ibm.auth.common.exception;

public class OtpNotFoundException extends RuntimeException{

    OtpNotFoundException(String message){
        super(message);
    }
}

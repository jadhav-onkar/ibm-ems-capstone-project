package com.ibm.payroll.common.exception;

public class PayslipNotFoundException extends RuntimeException {
    public PayslipNotFoundException(String message) {
        super(message);
    }
}

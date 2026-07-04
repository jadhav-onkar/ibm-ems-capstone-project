package com.ibm.payroll.common.exception;

public class PayrollAlreadyGeneratedException extends RuntimeException {
    public PayrollAlreadyGeneratedException(String message) {
        super(message);
    }
}

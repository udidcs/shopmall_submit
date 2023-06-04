package com.example.demo.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class ApikeyError {

    String errorcode;
    String errordetail;

    public ApikeyError(String errorcode, String errordetail) {
        this.errorcode = errorcode;
        this.errordetail = errordetail;
    }
}

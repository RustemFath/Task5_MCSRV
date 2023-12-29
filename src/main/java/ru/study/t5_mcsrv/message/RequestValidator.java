package ru.study.t5_mcsrv.message;

import lombok.Data;

@Data
public abstract class RequestValidator {
    private String failField;
    public abstract boolean isValidate();
}

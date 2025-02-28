package com.nhanpro.hello_springboot.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    KEY_INVALID(1002, "Invalid message key"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USERNAME_INVALID(1003, "User name must be at least 3 characters"),
    PASSWORD_INVALID(1004, "User name must be at least 6 characters"),
    USERNAME_NOT_EXISTED(1005, "User not existed"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    private int code;
    private String message  ;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

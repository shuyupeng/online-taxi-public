package com.shu.internalcommon.constant;

import lombok.Getter;

import java.awt.print.PrinterAbortException;

public enum CommonStatusEnum {

    /**
     * 验证码错误提示：1000-1099
     */
    verification_code_error(1099,"验证码不正确"),

    /**
     * 成功
     */
    SUCCESS(1,"success"),
    /**
     * 失败
     */
    FAIL(0,"fail")
    ;

    @Getter
    private int code;
    @Getter
    private String value;


    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

}

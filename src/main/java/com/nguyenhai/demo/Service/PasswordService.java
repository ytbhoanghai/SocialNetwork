package com.nguyenhai.demo.Service;

public interface PasswordService {

    Integer TIME_LOGOUT_CODE_EXPIRES = 30; // minutes
    String PREFIX_CHANGE_PASSWORD = "ytb:change.password:";

    String getCodeChangePassword(String idUser);

    Boolean verifyCodeChangePassword(String code, String idUser);

}

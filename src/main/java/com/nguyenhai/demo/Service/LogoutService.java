package com.nguyenhai.demo.Service;

public interface LogoutService {

    Integer TIME_THE_LOGOUT_CODE_EXPIRES = 30; // minutes
    String PREFIX_LOGOUT_ALL_DEVICES = "ytb:logout.all:";

    Boolean logoutAllDevices(String code);

    String getCodeLogoutAllDevices(String idUser);

}

package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInSuccessResponse {

    private String idUser;
    private String redirect;
    private Date dateCreated;

}

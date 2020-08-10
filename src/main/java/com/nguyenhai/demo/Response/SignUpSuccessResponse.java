package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpSuccessResponse {

    private String id;
    private String email;
    private Date dateCreate;
    private String redirect;

}

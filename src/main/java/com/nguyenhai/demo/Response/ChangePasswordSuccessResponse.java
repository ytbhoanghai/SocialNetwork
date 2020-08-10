package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordSuccessResponse {

    private String message;
    private String urlLogoutAllDevices;
    private Date dateCreated;
}

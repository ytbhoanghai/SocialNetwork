package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateImageSuccessResponse {

    private String src;
    private String message;
    private Date dateCreate;

}

package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityExceptionResponse {

    private String message;
    private HttpStatus status;
    private Date dateCreate;

}

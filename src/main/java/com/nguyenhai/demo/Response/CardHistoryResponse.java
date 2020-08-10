package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nguyenhai.demo.Entity.Notification.Type.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardHistoryResponse {

    private String id;
    private String content;
    private String url;
    private Date dateTime;


}

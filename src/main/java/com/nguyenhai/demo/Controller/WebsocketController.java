package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Util.MonitorUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebsocketController {

    private MonitorUserStatus monitorUserStatus;

    @Autowired
    public WebsocketController(MonitorUserStatus monitorUserStatus) {
        this.monitorUserStatus = monitorUserStatus;
    }

    @MessageMapping("me/online")
    public void keepAlive(Principal principal) {
        String email = principal.getName();
        monitorUserStatus.setIsOnline(email);
    }

}

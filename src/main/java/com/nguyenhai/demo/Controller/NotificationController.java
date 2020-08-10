package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController {

    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "{page}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotifications(@PathVariable Integer page, @PathVariable Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(notificationService.getNotifications(page, number, email));

    }

    @PostMapping(value = "viewed", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateViewedNotifications(@RequestBody List<String> ids, Principal principal) {
        String email = principal.getName();
        long response = notificationService.updateViewedNotifications(ids, email);
        return ResponseEntity.ok(String.valueOf(response));
    }

    @GetMapping(value = "number-unViewed", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getNumberNotificationsUnViewed(Principal principal) {
        String email = principal.getName();
        long response = notificationService.getNumberNotificationsUnViewed(email);
        return ResponseEntity.ok(String.valueOf(response));
    }

}

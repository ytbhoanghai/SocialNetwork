package com.nguyenhai.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "page")
public class BaseController {

    @GetMapping(value = "birthday")
    public String requestBirthdayPage() {
        return "birthday";
    }

    @GetMapping(value = "friend-list")
    public String requestFriendListPage() { return "friend-list"; }

    @GetMapping(value = "profile-images")
    public String requestProfileImagesPage() { return "profile-images"; }

    @GetMapping(value = "notification")
    public String requestNotificationPage() { return "notification"; }

    @GetMapping(value = "friend-request")
    public String requestFriendRequestPage() { return "friend-request"; }

    @GetMapping(value = "change-password")
    public String requestChangePasswordPage() { return "forgot-password"; };

    @GetMapping(value = "qanda")
    public String requestQuestionAndAnswerPage() { return "qanda"; }

}

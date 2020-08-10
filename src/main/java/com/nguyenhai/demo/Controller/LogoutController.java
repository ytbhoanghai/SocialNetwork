package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController {

    private LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @GetMapping(value = "all")
    public String logoutAllDevices(@RequestParam String code) {
        return logoutService.logoutAllDevices(code) ? "redirect:/logout" : "redirect:/error";
    }

}

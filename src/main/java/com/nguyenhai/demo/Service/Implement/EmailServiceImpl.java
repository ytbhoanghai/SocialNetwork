package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@Service(value = "emailService")
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;
    private InfoUserService infoUserService;
    private PasswordService passwordService;
    private LogoutService logoutService;
    private LoginService loginService;
    private FileService fileService;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender,
                            InfoUserService infoUserService,
                            PasswordService passwordService,
                            LogoutService logoutService,
                            LoginService loginService,
                            FileService fileService) {

        this.javaMailSender = javaMailSender;
        this.infoUserService = infoUserService;
        this.passwordService = passwordService;
        this.logoutService = logoutService;
        this.loginService = loginService;
        this.fileService = fileService;
    }

    @Override
    public void mailChangePassword(String email) throws IOException {
        InfoUser me = infoUserService.findByEmail(email);
        String code1 = passwordService.getCodeChangePassword(me.getId()); // code for change password
        String code2 = logoutService.getCodeLogoutAllDevices(me.getId()); // code for logout all devices
        sendMailTo(FILE_NAME_EMAIL_CHANGE_PASSWORD, Collections.singletonList(email), me.getFirstName(), code1, getUrlLogoutAllDevices(code2) );
    }

    @Override
    public void mailVerify(String email) throws IOException {
        InfoUser me = infoUserService.findByEmail(email);
        String code = loginService.getCodeVerify(me.getId());
        sendMailTo(FILE_NAME_EMAIL_VERIFY, Collections.singletonList(email), me.getFirstName(), getUrlVerify(code));
    }

    protected void sendMailTo(String fileName, List<String> tos, String... args) throws IOException {
        String fullContent = getContentEmailByFileName(fileName);

        String content = getContent(fullContent);
        for(int i = 0; i < args.length; i++) {
            content = content.replace("|" + (i + 1) + "|", args[i]);
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(tos.toArray(new String[0]));
        simpleMailMessage.setSubject(getSubject(fullContent));
        simpleMailMessage.setText(content);

        javaMailSender.send(simpleMailMessage);
    }

    private String getContentEmailByFileName(String fileName) throws IOException {
        if (!fileName.contains(".email")) {
            fileName = fileName.concat(".email");
        }
        File file = fileService.getFileInResource("dynamic/emails/" + fileName);
        return FileUtils.readFileToString(file, "UTF-8");
    }

    private String getSubject(String fullContent) {
        int index = fullContent.lastIndexOf("]]");
        return fullContent.substring(2, index);
    }

    private String getUrlVerify(String code) throws UnknownHostException {
        return getDomainName() + "/me/verify?code=" + code;
    }

    private String getUrlLogoutAllDevices(String code) throws UnknownHostException {
        return getDomainName() + "/logout/all?code=" + code;
    }

    private String getContent(String fullContent) {
        int index = fullContent.lastIndexOf("]]") + 4;
        return fullContent.substring(index);
    }

    private String getDomainName() throws UnknownHostException {
//        InetAddress addr = InetAddress.getLocalHost();
        return "ytbsocnet.tk";
    }
}

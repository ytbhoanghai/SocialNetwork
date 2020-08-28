package com.nguyenhai.demo.Service.Implement;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nguyenhai.demo.Entity.Account;
import com.nguyenhai.demo.Entity.Country;
import com.nguyenhai.demo.Entity.FriendRequest;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Form.SignUpForm;
import com.nguyenhai.demo.Repository.CountryRepository;
import com.nguyenhai.demo.Response.SignUpSuccessResponse;
import com.nguyenhai.demo.Service.*;
import com.nguyenhai.demo.Util.SequenceGeneratorUtil;
import com.restfb.types.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

// SERVICE DIRECT OF CONTROLLER
@Service(value = "signUpService")
public class SignUpServiceImpl implements SignUpService {

    private AccountService accountService;
    private InfoUserService infoUserService;
    private FriendRequestService friendRequestService;
    private FileService fileService;
    private CountryRepository countryRepository;

    @Autowired
    public SignUpServiceImpl(AccountService accountService,
                             InfoUserService infoUserService,
                             FriendRequestService friendRequestService,
                             FileService fileService,
                             CountryRepository countryRepository) {

        this.accountService = accountService;
        this.infoUserService = infoUserService;
        this.friendRequestService = friendRequestService;
        this.fileService = fileService;
        this.countryRepository = countryRepository;
    }

    @Override
    public SignUpSuccessResponse createAccount(SignUpForm form) throws IOException {
        return createAccount(null, form.getEmail(), form.getPassword(), form.getFirstName(), form.getLastName(), Account.TypeLogin.DEFAULT, null);
    }

    @Override
    public SignUpSuccessResponse createAccount(User user) throws IOException {
        String password = RandomStringUtils.random(18);
        String name = user.getName();
        String firstName = name.substring(name.lastIndexOf(' ') + 1);
        String lastName = name.substring(0, name.lastIndexOf(' '));

        return createAccount(user.getId(), user.getEmail(), password, firstName, lastName, Account.TypeLogin.FACEBOOK, user.getPicture().getUrl());
    }

    @Override
    public SignUpSuccessResponse createAccount(GoogleIdToken.Payload payload) throws IOException {
        String password = RandomStringUtils.random(18);
        String name = (String) payload.get("name");
        String firstName = name.substring(name.lastIndexOf(' ') + 1);
        String lastName = name.substring(0, name.lastIndexOf(' '));
        String urlAvatar = ((String) payload.get("picture")).replace("s96-c", "s350-c");

        return createAccount(payload.getSubject(), payload.getEmail(), password, firstName, lastName, Account.TypeLogin.GOOGLE, urlAvatar);
    }

    @Override
    public SignUpSuccessResponse createAccount(String id, String email, String password, String firstName, String lastName, Account.TypeLogin typeLogin, String urlAvatar) throws IOException {
        if (id == null) {
            id = SequenceGeneratorUtil.nextId();
        }
        Account account = Account.build(id, email, password, typeLogin);
        InfoUser infoUser = InfoUser.build(id, email, firstName, lastName);
        infoUser.setCountry(getCountryVietNam());

        if (urlAvatar == null) {
            fileService.generateAvatarByName(firstName, id);
        } else {
            fileService.generateAvatarByUrl(urlAvatar, id);
        }
        fileService.generateBackground(id);
        accountService.save(account);
        infoUserService.save(infoUser);
        friendRequestService.save(new FriendRequest(infoUser.getId(), new HashMap<>()));

        return new SignUpSuccessResponse(id, email, new Date(System.currentTimeMillis()), "/login");
    }

    private Country getCountryVietNam() {
        return countryRepository.findById("192")
                .orElse(null);
    }
}

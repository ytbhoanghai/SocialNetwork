package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.Account;
import com.nguyenhai.demo.Entity.Country;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Language;
import com.nguyenhai.demo.Exception.CountryNotFoundException;
import com.nguyenhai.demo.Form.ContactInformationForm;
import com.nguyenhai.demo.Form.NewPasswordForm;
import com.nguyenhai.demo.Form.PersonalInformationForm;
import com.nguyenhai.demo.Service.PasswordService;
import com.nguyenhai.demo.Repository.CountryRepository;
import com.nguyenhai.demo.Repository.LanguageRepository;
import com.nguyenhai.demo.Response.ChangePasswordSuccessResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;
import com.nguyenhai.demo.Service.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static com.nguyenhai.demo.Service.FileService.FORMAT_IMAGE_DEFAULT;
import static com.nguyenhai.demo.Service.FileService.PREFIX_PHOTO;

@Service(value = "settingService")
public class SettingServiceImpl implements SettingService {

    private FileService fileService;
    private InfoUserService infoUserService;
    private LanguageRepository languageRepository;
    private CountryRepository countryRepository;
    private PasswordService passwordService;
    private AccountService accountService;
    private LogoutService logoutService;
    private PostService postService;

    @Autowired
    public SettingServiceImpl(FileService fileService,
                              InfoUserService infoUserService,
                              LanguageRepository languageRepository,
                              CountryRepository countryRepository,
                              PasswordService passwordService,
                              AccountService accountService,
                              LogoutService logoutService,
                              PostService postService) {

        this.fileService = fileService;
        this.infoUserService = infoUserService;
        this.languageRepository = languageRepository;
        this.countryRepository = countryRepository;
        this.passwordService = passwordService;
        this.accountService = accountService;
        this.logoutService = logoutService;
        this.postService = postService;
    }

    @Override
    public UpdateImageSuccessResponse updateAvatarProfile(InputStream is, String email, Boolean createPost) throws IOException {
        InfoUser me = infoUserService.findByEmail(email);
        Path path = Paths.get(fileService.PATH_SAVE_AVATAR_DEFAULT.replace("{fileName}", me.getId() + "." + FORMAT_IMAGE_DEFAULT));
        Thumbnails.of(is)
                .antialiasing(Antialiasing.ON)
                .scale(1)
                .outputQuality(0.8f)
                .outputFormat("jpg")
                .toFile(path.toString());

        // for post
        if (createPost) {
            UUID id = UUID.randomUUID();
            Path path1 = Paths.get(fileService.PATH_SAVE_PHOTO.replace("{fileName}", PREFIX_PHOTO + "-" + id + "." + FORMAT_IMAGE_DEFAULT));
            InputStream avatar = new ByteArrayInputStream(fileService.getAvatar(me.getId()));
            Thumbnails.of(avatar).scale(1).outputQuality(1f).outputFormat("jpg").toFile(path1.toString());

            postService.createPostForAvatar("file/photo/" + id, email);
        }

        return new UpdateImageSuccessResponse(
                me.getUrlAvatar(),
                "update success avatar",
                new Date(System.currentTimeMillis()));
    }

    @Override
    public void updatePersonalInformation(PersonalInformationForm form, String email) {
        Country country = countryRepository.findById(form.getIdCountry())
                .orElseThrow(() -> new CountryNotFoundException(form.getIdCountry()));
        HashMap<String, Language> languages = new HashMap<>();
        languageRepository.findByIdIsIn(form.getIdLanguages())
                .forEach(language -> languages.put(language.getId(), language));

        InfoUser me = infoUserService.findByEmail(email);

        // setup info
        me.setFirstName(form.getFirstName());
        me.setLastName(form.getLastName());
        me.setBirthDay(form.getBirthDay());
        me.setGender(form.getGender());
        me.setCountry(country);
        me.setLanguages(languages);
        me.setAddress(form.getAddress());
        me.setFavoriteQuotes(form.getFavoriteQuotes());
        me.setOtherName(form.getOtherName());
        me.setAboutMe(form.getAboutMe());

        infoUserService.update(me);
    }

    @Override
    public ChangePasswordSuccessResponse changePassword(NewPasswordForm form, String email) {
        Account me = accountService.findByEmail(email);
        boolean flag = passwordService.verifyCodeChangePassword(form.getVerificationCode(), me.getId());
        Assert.isTrue(flag, "the verification code is invalid or has expired");

        String encodePass = new BCryptPasswordEncoder().encode(form.getNewPassword());
        me.setPassword(encodePass);

        accountService.update(me);

        return ChangePasswordSuccessResponse.builder()
                .message("change password success")
                .urlLogoutAllDevices("logout/all?code=" + logoutService.getCodeLogoutAllDevices(me.getId()))
                .dateCreated(new Date(System.currentTimeMillis())).build();
    }

    @Override
    public void updateContactInformation(ContactInformationForm form, String email) {
        HashMap<String, String> t = new HashMap<>();
        Arrays.asList("facebook", "twitter", "instagram", "googlePlus", "youtube", "linkedin")
                .forEach(s -> t.put(s, ""));
        t.keySet().forEach(s -> t.put(s, form.getSocialLinks().get(s)));

        InfoUser me = infoUserService.findByEmail(email);
        me.setSocialLinks(t);
        me.setMobile(form.getMobile());
        me.setWebsite(form.getWebsite());

        infoUserService.update(me);
    }

}

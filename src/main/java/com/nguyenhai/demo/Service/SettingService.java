package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Form.ContactInformationForm;
import com.nguyenhai.demo.Form.NewPasswordForm;
import com.nguyenhai.demo.Form.PersonalInformationForm;
import com.nguyenhai.demo.Response.ChangePasswordSuccessResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;

import java.io.IOException;
import java.io.InputStream;

public interface SettingService {

    UpdateImageSuccessResponse updateAvatarProfile(InputStream is, String email, Boolean createPost) throws IOException;

    void updatePersonalInformation(PersonalInformationForm personalInformationForm, String email);

    ChangePasswordSuccessResponse changePassword(NewPasswordForm newPasswordForm, String email);

    void updateContactInformation(ContactInformationForm contactInformationForm, String email);
}

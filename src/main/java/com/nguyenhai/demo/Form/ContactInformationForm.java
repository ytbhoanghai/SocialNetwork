package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.PhoneNumber;
import com.nguyenhai.demo.Annotation.SocialLink;
import com.nguyenhai.demo.Annotation.Url;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInformationForm {

    @SocialLink
    private HashMap<String, String> socialLinks;
    @PhoneNumber
    private String mobile;
    @Url
    private String website;

}

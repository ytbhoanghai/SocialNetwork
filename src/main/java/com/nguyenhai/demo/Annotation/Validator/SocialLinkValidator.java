package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.SocialLink;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class SocialLinkValidator implements ConstraintValidator<SocialLink, HashMap<String, String>> {

    private List<String> socials;
    private String patternFacebook;
    private String patternTwitter;
    private String patternInstagram;
    private String patternGooglePlus;
    private String patternYoutube;
    private String patternLinkedin;

    public SocialLinkValidator() {
        this.socials = Arrays.asList("facebook", "twitter", "instagram", "googlePlus", "youtube", "linkedin");
        patternFacebook = "^$|(?:(?:http|https):\\/\\/)?(?:www.)?facebook.com\\/(?:(?:\\w)*#!\\/)?(?:pages\\/)?(?:[?\\w\\-]*\\/)?(?:profile.php\\?id=(?=\\d.*))?([\\w\\-]*)?";
        patternTwitter = "^$|((?:http|https):\\/\\/)?(?:www\\.)?twitter\\.com\\/(?:(?:\\w)*#!\\/)?(?:pages\\/)?(?:[\\w\\-]*\\/)*([\\w\\-]*)";
        patternInstagram = "^$|(?:(?:http|https):\\/\\/)?(?:www\\.)?(?:instagram\\.com|instagr\\.am)\\/([A-Za-z0-9-_\\.]+)?";
        patternGooglePlus = "^$|((http|https):\\/\\/)?(www[.])?plus\\.google\\.com\\/.?\\/?.?\\/?([0-9]*)";
        patternYoutube = "^$|^(https?\\:\\/\\/)?((www\\.)?youtube\\.com|youtu\\.?be)\\/.*$";
        patternLinkedin = "^$|^https:\\/\\/[a-z]{2,3}\\.linkedin\\.com\\/.*$";
    }

    @Override
    public boolean isValid(HashMap<String, String> value, ConstraintValidatorContext context) {
        List<String> keys = new ArrayList<>(value.keySet());
        if (keys.containsAll(socials)) {
            for (Map.Entry<String, String> e : value.entrySet()) {
                switch (e.getKey()) {
                    case "facebook":
                        if (!e.getValue().matches(patternFacebook)) { return false; }
                        break;
                    case "twitter":
                        if (!e.getValue().matches(patternTwitter)) { return false; }
                        break;
                    case "instagram":
                        if (!e.getValue().matches(patternInstagram)) { return false; }
                        break;
                    case "googlePlus":
                        if (!e.getValue().matches(patternGooglePlus)) { return false; }
                        break;
                    case "youtube":
                        if (!e.getValue().matches(patternYoutube)) { return false; }
                        break;
                    case "linkedin":
                        if (!e.getValue().matches(patternLinkedin)) { return false; }
                }
            }
            return true;
        }
        return false;
    }
}

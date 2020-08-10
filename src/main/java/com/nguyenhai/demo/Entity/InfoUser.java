package com.nguyenhai.demo.Entity;

import com.nguyenhai.demo.Controller.FileController;
import com.nguyenhai.demo.Service.FileService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Document
@TypeAlias("info-user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoUser {

    private static final Boolean GENDER_DEFAULT = true;
    private static final Date BIRTHDAY_DEFAULT = new Date(650912400000L); // 1990/08/18

    @Id
    private String id;
    private String email;
    private String urlAvatar;
    private String urlBackground;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private String website;
    private String aboutMe;
    private String otherName;
    private String favoriteQuotes;
    private Boolean gender;
    private Boolean isValidEmail;
    private Date birthDay;
    private HashMap<String, String> socialLinks; // id and url
    @DBRef
    private Country country;
    private RelationshipInfo relationshipInfo;
    @DBRef
    private HashMap<String, Skill> skills; // id and skill
    private HashMap<String, CollegeInfo> collegeInfos; // id and collegeInfo
    private HashMap<String, Language> languages; // id and language
    private HashMap<String, FamilyMemberInfo> familyMemberInfos; // id and familyMemberInfo
    private HashMap<String, WorkPlaceInfo> workPlaceInfos; // id and workPlaceInfo
    @DBRef
    private PlaceLived currentCity;
    @DBRef
    private PlaceLived homeTown;
    @DBRef
    private HashMap<String, PlaceLived> otherPlacesLived; // id and placeLived
    private HashMap<String, FriendInfo> currentListFriendInfo; // id and friendInfo
    private HashMap<String, FriendInfo> blockedListFriendInfo; // id and friendInfo
    private List<String> listFollower;
    private List<String> listFollowing;

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public static InfoUser build(String id, String email, String firstName, String lastName) {
        String _urlAvatar = FileService.PATTERN_URI_DOWNLOAD_AVATAR.replace("{idUser}", id);
        String _urlBackground = FileService.PATTERN_URI_DOWNLOAD_BACKGROUND.replace("{idUser}", id);

        return InfoUser.builder()
                .id(id)
                .email(email)
                .urlAvatar(_urlAvatar)
                .urlBackground(_urlBackground)
                .firstName(firstName)
                .lastName(lastName)
                .isValidEmail(Account.IS_VALID_EMAIL_DEFAULT)
                .gender(GENDER_DEFAULT)
                .birthDay(BIRTHDAY_DEFAULT)
                .socialLinks(new HashMap<>())
                .skills(new HashMap<>())
                .collegeInfos(new HashMap<>())
                .languages(new HashMap<>())
                .familyMemberInfos(new HashMap<>())
                .workPlaceInfos(new HashMap<>())
                .otherPlacesLived(new HashMap<>())
                .currentListFriendInfo(new HashMap<>())
                .blockedListFriendInfo(new HashMap<>())
                .listFollower(new ArrayList<>())
                .listFollowing(new ArrayList<>()).build();
    }

    @Data
    @AllArgsConstructor
    public static class RelationshipInfo {
        @DBRef
        private PersonalRelationship personalRelationship;
        private String withUserId;
        private Date dateCreated;
    }

    @Data
    @AllArgsConstructor
    public static class FamilyMemberInfo {
        @DBRef
        private FamilyRelationship familyRelationship;
        private String withUserId;
        private Date dateCreated;
    }

    @Data
    @AllArgsConstructor
    public static class WorkPlaceInfo {
        @DBRef
        private WorkPlace workPlace;
        @DBRef
        private JobPosition jobPosition;
        private Date dateCreate;
    }

    @Data
    @AllArgsConstructor
    public static class CollegeInfo {
        @DBRef
        private College college;
        private Date dateStart;
        private Date dateEnd;
    }

    @Data
    @AllArgsConstructor
    public static class FriendInfo {
        private String idFriend;
        private Date dateAddFriend;
    }
}

package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.PlaceLived;
import com.nguyenhai.demo.Form.CollegeForm;
import com.nguyenhai.demo.Form.FamilyMemberForm;
import com.nguyenhai.demo.Form.RelationshipForm;
import com.nguyenhai.demo.Form.WorkPlaceForm;
import com.nguyenhai.demo.Response.FamilyMemberInfoResponse;
import com.nguyenhai.demo.Response.RelationshipInfoResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;

import java.io.IOException;
import java.io.InputStream;

public interface ProfileService {

    RelationshipInfoResponse addRelationship(RelationshipForm relationshipForm, String email);

    void deleteRelationship(String email);

    FamilyMemberInfoResponse addFamilyMember(FamilyMemberForm familyMemberForm, String email);

    void deleteFamilyMember(String id, String email);

    FamilyMemberInfoResponse updateFamilyMember(FamilyMemberForm familyMemberForm, String email);

    InfoUser.WorkPlaceInfo addWorkPlace(WorkPlaceForm workPlaceForm, String email);

    void deleteWorkPlace(String id, String email);

    InfoUser.WorkPlaceInfo updateWorkPlace(WorkPlaceForm workPlaceForm, String email);

    void addSkill(String id, String email);

    void deleteSkill(String id, String email);

    InfoUser.CollegeInfo updateCollege(CollegeForm collegeForm, String email);

    void deleteCollege(String id, String email);

    InfoUser.CollegeInfo addCollege(CollegeForm collegeForm, String email);

    PlaceLived addHometown(String id, String email);

    void deleteHometown(String email);

    PlaceLived addCurrentCity(String id, String email);

    void deleteCurrentCity(String email);

    PlaceLived addOtherPlaceLived(String id, String email);

    void deleteOtherPlaceLived(String id, String email);

    UpdateImageSuccessResponse updateBackgroundProfile(InputStream isImage, String email) throws IOException;
}

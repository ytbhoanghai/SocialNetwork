package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.*;
import com.nguyenhai.demo.Exception.*;
import com.nguyenhai.demo.Form.CollegeForm;
import com.nguyenhai.demo.Form.FamilyMemberForm;
import com.nguyenhai.demo.Form.RelationshipForm;
import com.nguyenhai.demo.Form.WorkPlaceForm;
import com.nguyenhai.demo.Repository.*;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.FamilyMemberInfoResponse;
import com.nguyenhai.demo.Response.RelationshipInfoResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;
import com.nguyenhai.demo.Service.FileService;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.ProfileService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service(value = "profileService")
public class ProfileServiceImpl implements ProfileService {

    private InfoUserService infoUserService;
    private PersonalRelationshipRepository personalRelationshipRepository;
    private FamilyRelationshipRepository familyRelationshipRepository;
    private WorkPlaceRepository workPlaceRepository;
    private JobPositionRepository jobPositionRepository;
    private SkillRepository skillRepository;
    private CollegeRepository collegeRepository;
    private PlaceLivedRepository placeLivedRepository;
    private FileService fileService;

    @Autowired
    public ProfileServiceImpl(InfoUserService infoUserService,
                              PersonalRelationshipRepository personalRelationshipRepository,
                              FamilyRelationshipRepository familyRelationshipRepository,
                              WorkPlaceRepository workPlaceRepository,
                              JobPositionRepository jobPositionRepository,
                              SkillRepository skillRepository,
                              CollegeRepository collegeRepository,
                              PlaceLivedRepository placeLivedRepository,
                              FileService fileService) {

        this.infoUserService = infoUserService;
        this.personalRelationshipRepository = personalRelationshipRepository;
        this.familyRelationshipRepository = familyRelationshipRepository;
        this.workPlaceRepository = workPlaceRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.skillRepository = skillRepository;
        this.collegeRepository = collegeRepository;
        this.placeLivedRepository = placeLivedRepository;

        this.fileService = fileService;
    }

    @Override
    public RelationshipInfoResponse addRelationship(RelationshipForm relationshipForm, String email) {
        PersonalRelationship personalRelationship = personalRelationshipRepository
                .findById(relationshipForm.getIdPersonalRelationship())
                .orElseThrow(() -> new PersonalRelationshipNotFoundException(relationshipForm.getIdPersonalRelationship()));

        InfoUser me = infoUserService.findByEmail(email);
        InfoUser myFriend = null;
        String idMyFriend = null;
        if (personalRelationship.getId().equals("PR2")) { // is married
            myFriend = infoUserService.findById(relationshipForm.getWithUserId());
            idMyFriend = myFriend.getId();
        }

        InfoUser.RelationshipInfo relationshipInfo = new InfoUser.RelationshipInfo(
                personalRelationship,
                idMyFriend,
                new Date(System.currentTimeMillis()));

        me.setRelationshipInfo(relationshipInfo);
        infoUserService.update(me);

        return new RelationshipInfoResponse(
                personalRelationship,
                myFriend == null ? null : BasicUserInfoResponse.build(myFriend),
                relationshipInfo.getDateCreated());
    }

    @Override
    public void deleteRelationship(String email) {
        InfoUser infoUser = infoUserService.findByEmail(email);
        infoUser.setRelationshipInfo(null);
        infoUserService.update(infoUser);
    }

    @Override
    public FamilyMemberInfoResponse addFamilyMember(FamilyMemberForm familyMemberForm, String email) {
        FamilyRelationship familyRelationship = familyRelationshipRepository
                .findById(familyMemberForm.getIdFamilyRelationship())
                .orElseThrow(() -> new FamilyRelationshipNotFoundException(familyMemberForm.getIdFamilyRelationship()));

        InfoUser me = infoUserService.findByEmail(email);
        InfoUser myFriend = infoUserService.findById(familyMemberForm.getWithUserId());

        boolean flag = me.getFamilyMemberInfos().containsKey(familyMemberForm.getWithUserId());
        Assert.isTrue(!flag, "this member already exists in the family members list");

        InfoUser.FamilyMemberInfo familyMemberInfo = new InfoUser.FamilyMemberInfo(familyRelationship, myFriend.getId(), new Date(System.currentTimeMillis()));

        me.getFamilyMemberInfos().put(myFriend.getId(), familyMemberInfo);
        infoUserService.update(me);

        return new FamilyMemberInfoResponse(
                familyRelationship,
                BasicUserInfoResponse.build(myFriend),
                familyMemberInfo.getDateCreated());
    }

    @Override
    public void deleteFamilyMember(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getFamilyMemberInfos().containsKey(id);
        Assert.isTrue(flag, "this relationship not exists in the \"family member info list\"");

        me.getFamilyMemberInfos().remove(id);
        infoUserService.update(me);
    }

    @Override
    public FamilyMemberInfoResponse updateFamilyMember(FamilyMemberForm familyMemberForm, String email) {
        FamilyRelationship familyRelationship = familyRelationshipRepository
                .findById(familyMemberForm.getIdFamilyRelationship())
                .orElseThrow(() -> new FamilyRelationshipNotFoundException(familyMemberForm.getIdFamilyRelationship()));

        InfoUser me = infoUserService.findByEmail(email);
        InfoUser myFriend = infoUserService.findById(familyMemberForm.getWithUserId());

        boolean flag = me.getFamilyMemberInfos().containsKey(familyMemberForm.getWithUserId());
        Assert.isTrue(flag, "user with id " + myFriend.getId() + " does not exist in the list family member");

        InfoUser.FamilyMemberInfo familyMemberInfo = me.getFamilyMemberInfos().get(myFriend.getId());
        familyMemberInfo.setFamilyRelationship(familyRelationship);
        infoUserService.update(me);

        return new FamilyMemberInfoResponse(
                familyRelationship,
                BasicUserInfoResponse.build(myFriend),
                familyMemberInfo.getDateCreated());
    }

    @Override
    public InfoUser.WorkPlaceInfo addWorkPlace(WorkPlaceForm workPlaceForm, String email) {
        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getWorkPlaceInfos().containsKey(workPlaceForm.getIdWorkPlace());
        Assert.isTrue(!flag, "this \"work place\" already exist in the \"work place list\"");

        WorkPlace workPlace = workPlaceRepository.findById(workPlaceForm.getIdWorkPlace())
                .orElseThrow(() -> new WorkPlaceNotFoundException(workPlaceForm.getIdWorkPlace()));
        JobPosition jobPosition = jobPositionRepository.findById(workPlaceForm.getIdJobPosition())
                .orElseThrow(() -> new JobPositionNotFoundException(workPlaceForm.getIdJobPosition()));

        InfoUser.WorkPlaceInfo workPlaceInfo = new InfoUser.WorkPlaceInfo(workPlace, jobPosition, new Date(System.currentTimeMillis()));

        me.getWorkPlaceInfos().put(workPlace.getId(), workPlaceInfo);
        infoUserService.update(me);

        return workPlaceInfo;
    }

    @Override
    public void deleteWorkPlace(String id, String email) {
        WorkPlace workPlace = workPlaceRepository.findById(id)
                .orElseThrow(() -> new WorkPlaceNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getWorkPlaceInfos().containsKey(workPlace.getId());
        Assert.isTrue(flag, "this work place not exists in the \"work place info list\"");

        me.getWorkPlaceInfos().remove(id);
        infoUserService.update(me);
    }

    @Override
    public InfoUser.WorkPlaceInfo updateWorkPlace(WorkPlaceForm workPlaceForm, String email) {
        WorkPlace workPlace = workPlaceRepository.findById(workPlaceForm.getIdWorkPlace())
                .orElseThrow(() -> new WorkPlaceNotFoundException(workPlaceForm.getIdWorkPlace()));
        JobPosition jobPosition = jobPositionRepository.findById(workPlaceForm.getIdJobPosition())
                .orElseThrow(() -> new JobPositionNotFoundException(workPlaceForm.getIdJobPosition()));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getWorkPlaceInfos().containsKey(workPlace.getId());
        Assert.isTrue(flag, "this work place not exists in the \"work place info list\"");

        InfoUser.WorkPlaceInfo workPlaceInfo = me.getWorkPlaceInfos().get(workPlace.getId());
        workPlaceInfo.setJobPosition(jobPosition);

        infoUserService.update(me);

        return workPlaceInfo;
    }

    @Override
    public void addSkill(String id, String email) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getSkills().containsKey(skill.getId());
        Assert.isTrue(!flag, "this skill already exist in the skill list");
        Assert.isTrue(me.getSkills().size() <= 8, "the list has reached its maximum size");

        me.getSkills().put(skill.getId(), skill);
        infoUserService.update(me);
    }

    @Override
    public void deleteSkill(String id, String email) {
        Skill skill= skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getSkills().containsKey(skill.getId());
        Assert.isTrue(flag, "this skill not exists in the skill list");

        me.getSkills().remove(skill.getId());
        infoUserService.update(me);
    }

    @Override
    public InfoUser.CollegeInfo updateCollege(CollegeForm collegeForm, String email) {
        College college = collegeRepository.findById(collegeForm.getIdCollege())
                .orElseThrow(() -> new CollegeNotFoundException(collegeForm.getIdCollege()));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getCollegeInfos().containsKey(college.getId());
        Assert.isTrue(flag, "this college not exists in the college list");

        InfoUser.CollegeInfo collegeInfo = me.getCollegeInfos().get(college.getId());
        collegeInfo.setDateStart(collegeForm.getDateStart());
        collegeInfo.setDateEnd(collegeForm.getDateEnd());
        infoUserService.update(me);

        return collegeInfo;
    }

    @Override
    public void deleteCollege(String id, String email) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getCollegeInfos().containsKey(college.getId());
        Assert.isTrue(flag, "this college not exists in the college list");

        me.getCollegeInfos().remove(college.getId());
        infoUserService.update(me);
    }

    @Override
    public InfoUser.CollegeInfo addCollege(CollegeForm collegeForm, String email) {
        boolean flag = collegeForm.getDateStart().before(collegeForm.getDateEnd());
        Assert.isTrue(flag, "time is invalid");

        College college = collegeRepository.findById(collegeForm.getIdCollege())
                .orElseThrow(() -> new CollegeNotFoundException(collegeForm.getIdCollege()));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag2 = me.getCollegeInfos().containsKey(college.getId());
        Assert.isTrue(!flag2, "this college already exists in the college list");

        InfoUser.CollegeInfo collegeInfo = new InfoUser.CollegeInfo(college, collegeForm.getDateStart(), collegeForm.getDateEnd());

        me.getCollegeInfos().put(college.getId(), collegeInfo);
        infoUserService.update(me);

        return collegeInfo;
    }

    @Override
    public PlaceLived addHometown(String id, String email) {
        PlaceLived placeLived = placeLivedRepository.findById(id)
                .orElseThrow(() -> new PlaceLivedNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);
        me.setHomeTown(placeLived);
        infoUserService.update(me);

        return placeLived;
    }

    @Override
    public void deleteHometown(String email) {
        InfoUser me = infoUserService.findByEmail(email);
        me.setHomeTown(null);
        infoUserService.update(me);
    }

    @Override
    public PlaceLived addCurrentCity(String id, String email) {
        PlaceLived placeLived = placeLivedRepository.findById(id)
                .orElseThrow(() -> new PlaceLivedNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);
        me.setCurrentCity(placeLived);
        infoUserService.update(me);

        return placeLived;
    }

    @Override
    public void deleteCurrentCity(String email) {
        InfoUser me = infoUserService.findByEmail(email);
        me.setCurrentCity(null);
        infoUserService.update(me);
    }

    @Override
    public PlaceLived addOtherPlaceLived(String id, String email) {
        PlaceLived placeLived = placeLivedRepository.findById(id)
                .orElseThrow(() -> new PlaceLivedNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getOtherPlacesLived().containsKey(placeLived.getId());
        Assert.isTrue(!flag, "this \"place lived\" already exist in the \"place lived list\"");
        Assert.isTrue(me.getOtherPlacesLived().size() <= 8, "the list has reached its maximum size");

        me.getOtherPlacesLived().put(placeLived.getId(), placeLived);
        infoUserService.update(me);

        return placeLived;
    }

    @Override
    public void deleteOtherPlaceLived(String id, String email) {
        PlaceLived placeLived = placeLivedRepository.findById(id)
                .orElseThrow(() -> new PlaceLivedNotFoundException(id));

        InfoUser me = infoUserService.findByEmail(email);

        boolean flag = me.getOtherPlacesLived().containsKey(placeLived.getId());
        Assert.isTrue(flag, "this \"place lived\" not exist in the \"place lived list\"");

        me.getOtherPlacesLived().remove(placeLived.getId());
        infoUserService.update(me);
    }

    @Override
    public UpdateImageSuccessResponse updateBackgroundProfile(InputStream isImage, String email) throws IOException {
        InfoUser me = infoUserService.findByEmail(email);
        Path path = Paths.get(fileService.PATH_SAVE_BACKGROUND_DEFAULT.replace("{fileName}", me.getId() + "." + fileService.FORMAT_IMAGE_DEFAULT));

        Thumbnails.of(isImage)
                .scale(1)
                .outputQuality(0.7f)
                .antialiasing(Antialiasing.ON)
                .outputFormat("jpg")
                .toFile(path.toString());

        return new UpdateImageSuccessResponse(
                me.getUrlBackground(),
                "update success background profile",
                new Date(System.currentTimeMillis()));
    }
}

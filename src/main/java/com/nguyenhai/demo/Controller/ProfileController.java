package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.PlaceLived;
import com.nguyenhai.demo.Form.CollegeForm;
import com.nguyenhai.demo.Form.FamilyMemberForm;
import com.nguyenhai.demo.Form.RelationshipForm;
import com.nguyenhai.demo.Form.WorkPlaceForm;
import com.nguyenhai.demo.Response.FamilyMemberInfoResponse;
import com.nguyenhai.demo.Response.RelationshipInfoResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;
import com.nguyenhai.demo.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public String requestPageProfile() {
        return "profile";
    }

    @PostMapping(value = "relationship", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRelationship(@Valid @RequestBody RelationshipForm relationshipForm, Principal principal) {
        String email = principal.getName();
        RelationshipInfoResponse response = profileService.addRelationship(relationshipForm, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "relationship", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteRelationship(Principal principal) {
        String email = principal.getName();
        profileService.deleteRelationship(email);
        return ResponseEntity.ok("delete success relationship");
    }

    @PostMapping(value = "family-member", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFamilyMember(@Valid @RequestBody FamilyMemberForm familyMemberForm, Principal principal) {
        String email = principal.getName();
        FamilyMemberInfoResponse response = profileService.addFamilyMember(familyMemberForm, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "family-member", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteFamilyMember(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.deleteFamilyMember(id, email);
        return ResponseEntity.ok("delete success family member");
    }

    @PutMapping(value = "family-member", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFamilyMember(@Valid @RequestBody FamilyMemberForm familyMemberForm, Principal principal) {
        String email = principal.getName();
        FamilyMemberInfoResponse response = profileService.updateFamilyMember(familyMemberForm, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "work-place", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addWorkPlace(@Valid @RequestBody WorkPlaceForm workPlaceForm, Principal principal) {
        String email = principal.getName();
        InfoUser.WorkPlaceInfo response = profileService.addWorkPlace(workPlaceForm, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "work-place", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteWorkPlace(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.deleteWorkPlace(id, email);
        return ResponseEntity.ok("delete success work place");
    }

    @PutMapping(value = "work-place", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWorkPlace(@Valid @RequestBody WorkPlaceForm workPlaceForm, Principal principal) {
        String email = principal.getName();
        InfoUser.WorkPlaceInfo response = profileService.updateWorkPlace(workPlaceForm, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "skill", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> addSkill(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.addSkill(id, email);
        return ResponseEntity.ok("add skill success");
    }

    @DeleteMapping(value = "skill", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteSkill(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.deleteSkill(id, email);
        return ResponseEntity.ok("delete success skill");
    }

    @PostMapping(value = "college", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCollege(@Valid @RequestBody CollegeForm collegeForm, Principal principal) {
        String email = principal.getName();
        InfoUser.CollegeInfo response = profileService.addCollege(collegeForm, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "college", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteCollege(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.deleteCollege(id, email);
        return ResponseEntity.ok("delete success college");
    }

    @PutMapping(value = "college", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCollege(@Valid @RequestBody CollegeForm collegeForm, Principal principal) {
        String email = principal.getName();
        InfoUser.CollegeInfo response = profileService.updateCollege(collegeForm, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "hometown", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addHometown(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        PlaceLived response = profileService.addHometown(id, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "hometown", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteHometown(Principal principal) {
        String email = principal.getName();
        profileService.deleteHometown(email);
        return ResponseEntity.ok("delete success hometown");
    }

    @PostMapping(value = "current-city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCurrentCity(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        PlaceLived response = profileService.addCurrentCity(id, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "current-city", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteCurrentCity(Principal principal) {
        String email = principal.getName();
        profileService.deleteCurrentCity(email);
        return ResponseEntity.ok("delete success current city");
    }

    @PostMapping(value = "other-place-lived", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOtherPlaceLived(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        PlaceLived response = profileService.addOtherPlaceLived(id, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "other-place-lived", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteOtherPlaceLived(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        profileService.deleteOtherPlaceLived(id, email);
        return ResponseEntity.ok("delete success other place lived");
    }

    @PostMapping(value = "background", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBackgroundProfile(@RequestParam String bgBase64, Principal principal) throws IOException {
        String email = principal.getName();
        InputStream isImage = new ByteArrayInputStream(Base64.getDecoder().decode(bgBase64));
        UpdateImageSuccessResponse response = profileService.updateBackgroundProfile(isImage, email);
        return ResponseEntity.ok(response);
    }
}

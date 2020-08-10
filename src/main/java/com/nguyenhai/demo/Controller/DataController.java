package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Entity.*;
import com.nguyenhai.demo.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/data")
public class DataController {

    private DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "personal-relationships", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListPersonalRelationship() {
        return ResponseEntity.ok(dataService.getListPersonalRelationship());
    }

    @GetMapping(value = "family-relationships", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListFamilyRelationship() {
        return ResponseEntity.ok(dataService.getListFamilyRelationship());
    }

    @GetMapping(value = "work-place", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListWorkPlaceByTerm(@RequestParam String term) {
        return ResponseEntity.ok(dataService.getListWorkPlaceByTerm(term));
    }

    @GetMapping(value = "job-position", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListJobPositionByTerm(@RequestParam String term) {
        return ResponseEntity.ok(dataService.getListJobPositionByTerm(term));
    }

    @GetMapping(value = "skill", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListSkillByTerm(@RequestParam String term) {
        return ResponseEntity.ok(dataService.getListSkillByTerm(term));
    }

    @GetMapping(value = "college", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListCollegeByTerm(@RequestParam String term) {
        return ResponseEntity.ok(dataService.getListCollegeByTerm(term));
    }

    @GetMapping(value = "place-lived", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListPlaceLivedByTerm(@RequestParam String term) {
        return ResponseEntity.ok(dataService.getListPlaceLivedByTerm(term));
    }

    @GetMapping(value = "language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListLanguage() {
        return ResponseEntity.ok(dataService.getListLanguage());
    }

    @GetMapping(value = "country", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListCountry() {
        return ResponseEntity.ok(dataService.getListCountry());
    }

    @GetMapping(value = "feeling", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListFeeling() {
        return ResponseEntity.ok(dataService.getListFeeling());
    }

    @GetMapping(value = "qanda", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListQuestionAndAnswer() {
        return ResponseEntity.ok(dataService.getListQuestionAndAnswer());
    }

}

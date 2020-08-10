package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.*;

import java.util.List;

public interface DataService {

    List<PersonalRelationship> getListPersonalRelationship();

    List<FamilyRelationship> getListFamilyRelationship();

    List<WorkPlace> getListWorkPlaceByTerm(String term);

    List<JobPosition> getListJobPositionByTerm(String term);

    List<Skill> getListSkillByTerm(String term);

    List<College> getListCollegeByTerm(String term);

    List<PlaceLived> getListPlaceLivedByTerm(String term);

    List<Language> getListLanguage();

    List<Country> getListCountry();

    List<Feeling> getListFeeling();

    List<QuestionAndAnswer> getListQuestionAndAnswer();
}

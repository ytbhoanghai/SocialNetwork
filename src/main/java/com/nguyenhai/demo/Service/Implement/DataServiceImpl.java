package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.*;
import com.nguyenhai.demo.Repository.*;
import com.nguyenhai.demo.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "dataService")
public class DataServiceImpl implements DataService {

    private PersonalRelationshipRepository personalRelationshipRepository;
    private FamilyRelationshipRepository familyRelationshipRepository;
    private WorkPlaceRepository workPlaceRepository;
    private JobPositionRepository jobPositionRepository;
    private SkillRepository skillRepository;
    private CollegeRepository collegeRepository;
    private PlaceLivedRepository placeLivedRepository;
    private LanguageRepository languageRepository;
    private CountryRepository countryRepository;
    private FeelingRepository feelingRepository;
    private QuestionAndAnswerRepository questionAndAnswerRepository;

    @Autowired
    public DataServiceImpl(PersonalRelationshipRepository personalRelationshipRepository,
                           FamilyRelationshipRepository familyRelationshipRepository,
                           WorkPlaceRepository workPlaceRepository,
                           JobPositionRepository jobPositionRepository,
                           SkillRepository skillRepository,
                           CollegeRepository collegeRepository,
                           PlaceLivedRepository placeLivedRepository,
                           LanguageRepository languageRepository,
                           CountryRepository countryRepository,
                           FeelingRepository feelingRepository,
                           QuestionAndAnswerRepository questionAndAnswerRepository) {

        this.personalRelationshipRepository = personalRelationshipRepository;
        this.familyRelationshipRepository = familyRelationshipRepository;
        this.workPlaceRepository = workPlaceRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.skillRepository = skillRepository;
        this.collegeRepository = collegeRepository;
        this.placeLivedRepository = placeLivedRepository;
        this.languageRepository = languageRepository;
        this.countryRepository = countryRepository;
        this.feelingRepository = feelingRepository;
        this.questionAndAnswerRepository = questionAndAnswerRepository;
    }

    @Override
    public List<PersonalRelationship> getListPersonalRelationship() {
        return personalRelationshipRepository.findAll();
    }

    @Override
    public List<FamilyRelationship> getListFamilyRelationship() { return familyRelationshipRepository.findAll(); }

    @Override
    public List<WorkPlace> getListWorkPlaceByTerm(String term) { return workPlaceRepository.findByNameIsLike(term); }

    @Override
    public List<JobPosition> getListJobPositionByTerm(String term) { return jobPositionRepository.findByNameIsLike(term); }

    @Override
    public List<Skill> getListSkillByTerm(String term) { return skillRepository.findByNameIsLike(term); }

    @Override
    public List<College> getListCollegeByTerm(String term) { return collegeRepository.findByNameIsLike(term); }

    @Override
    public List<PlaceLived> getListPlaceLivedByTerm(String term) { return placeLivedRepository.findByNameIsLike(term); }

    @Override
    public List<Language> getListLanguage() { return languageRepository.findAll(); }

    @Override
    public List<Country> getListCountry() { return countryRepository.findAll(); }

    @Override
    public List<Feeling> getListFeeling() { return feelingRepository.findAll(); }

    @Override
    public List<QuestionAndAnswer> getListQuestionAndAnswer() {
        return questionAndAnswerRepository.findAll();
    }
}

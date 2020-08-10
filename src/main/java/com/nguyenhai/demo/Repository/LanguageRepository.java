package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Language;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends MongoRepository<Language, String> {

    List<Language> findByIdIsIn(List<String> ids);

}

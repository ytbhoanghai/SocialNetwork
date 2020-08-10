package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.QuestionAndAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAndAnswerRepository extends MongoRepository<QuestionAndAnswer, String> {
}

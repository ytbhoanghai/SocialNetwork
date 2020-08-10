package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {

    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<Skill> findByNameIsLike(String term);
}

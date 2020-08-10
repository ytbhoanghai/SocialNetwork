package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.PersonalRelationship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRelationshipRepository extends MongoRepository<PersonalRelationship, String> {
}

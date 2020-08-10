package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.FamilyRelationship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRelationshipRepository extends MongoRepository<FamilyRelationship, String> {
}

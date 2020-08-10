package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Feeling;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeelingRepository extends MongoRepository<Feeling, String> {
}

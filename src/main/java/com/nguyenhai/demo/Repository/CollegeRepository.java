package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.College;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends MongoRepository<College, String> {

    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<College> findByNameIsLike(String term);

}

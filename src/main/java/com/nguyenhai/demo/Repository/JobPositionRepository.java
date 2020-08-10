package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.JobPosition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends MongoRepository<JobPosition, String> {

    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<JobPosition> findByNameIsLike(String term);

}

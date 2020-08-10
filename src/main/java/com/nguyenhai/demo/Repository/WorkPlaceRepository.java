package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.WorkPlace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkPlaceRepository extends MongoRepository<WorkPlace, String> {

    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<WorkPlace> findByNameIsLike(String term);

}

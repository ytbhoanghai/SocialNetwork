package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.PlaceLived;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceLivedRepository extends MongoRepository<PlaceLived, String> {

    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<PlaceLived> findByNameIsLike(String term);

}

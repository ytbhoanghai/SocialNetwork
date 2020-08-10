package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.PhotoGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoGroupRepository extends MongoRepository<PhotoGroup, String> {

    void deleteByPostId(String postId);

    List<PhotoGroup> findAllByIdAuthor(String idAuthor, Pageable pageable);

}

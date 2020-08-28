package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.InfoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfoUserRepository extends MongoRepository<InfoUser, String> {

    Optional<InfoUser> findByEmail(String email);

    List<InfoUser> findByIdIsIn(List<String> ids);

    List<InfoUser> findAllByIdIsNotIn(List<String> ids);

}

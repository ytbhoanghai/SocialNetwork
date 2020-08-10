package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {

}

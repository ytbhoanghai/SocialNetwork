package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.ChatBox;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatBoxRepository extends MongoRepository<ChatBox, String> {

/*    @Query("{$or: [" +
            "{$and: [{idAuthor: ?0}, {idUser: ?1}]}," +
            "{$and: [{idAuthor: ?1}, {idUser: ?0}]}" +
            "]}")*/
    Optional<ChatBox> findByIdAuthorAndIdUser(String id1, String id2);

    List<ChatBox> findAllByIdAuthor(String idAuthor, Sort sort);

    List<ChatBox> findAllByIdAuthorAndUnSeenGreaterThan(String idAuthor, Integer number, Sort sort);

}

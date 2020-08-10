package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByIdChatBox(String idChatBox, Sort sort);

    void deleteByIdChatBox(String idChatBox);

}

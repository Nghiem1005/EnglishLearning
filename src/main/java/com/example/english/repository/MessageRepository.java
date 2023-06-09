package com.example.english.repository;

import com.example.english.entities.Message;
import com.example.english.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findMessagesBySendUserAndReceiverUser(User sender, User receiver);

  @Query(value = "SELECT * FROM tbl_message t1 WHERE t1.id = "
      + "( SELECT MAX(t2.id) FROM tbl_message t2 WHERE t2.receiver_user_id = t1.receiver_user_id) "
      + "and receiver_user_id = 2", nativeQuery = true)
  List<Message> findMessagesBySendUser(@Param("sendUser") Long sendUser);
}

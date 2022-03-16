package com.dongdong.backend.repository;

import com.dongdong.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Modifying
//    @Query("insert into user (email,password) values (?1,?2)")
//    int addUserByEmail(String email,String password);

    List<User> findByUserNameLike(String userName);

    Optional<User> findByUserId(Long id);
}

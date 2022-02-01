package com.dongdong.backend.Repository;

import com.dongdong.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//    @Modifying
//    @Query("insert into user (email,password) values (?1,?2)")
//    int addUserByEmail(String email,String password);
}

package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {

    Optional<Users> findById(Long aLong);

    Optional<Users> findByemail(String username);
    @Query("SELECT u.fcmToken FROM User u WHERE u.id = :userId")
    String findFcmTokenByUserId(@Param("userId") Long userId);


}


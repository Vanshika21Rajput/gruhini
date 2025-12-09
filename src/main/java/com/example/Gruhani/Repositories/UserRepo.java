package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,String> {
    Users findByemail(String username);
}


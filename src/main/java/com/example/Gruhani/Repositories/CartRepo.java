package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

     Cart findByu_id(Long uId);

   Cart findByu_email(String username);

   @Query("""
           Select distinct  c from Cart c JOIN FETCH c.l ci JOIN FETCH ci.p where c.u.email= :email
           """)
    Cart findCartandRelatedFields(@Param("email") String username);
}

package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.FavoriteRepo;
import com.example.Gruhani.models.Favorite;
import com.example.Gruhani.service.authutil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FavoriteController {

    @Autowired
    FavoriteRepo favoriteRepo;

    @Autowired
    authutil authUtil;

    @PostMapping("/add-favorite")
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Object> payload, HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        List<Object> auth = authUtil.validatetoken(token);
        String email = (String) auth.get(0);

        String dishId = (String) payload.get("dishId");
        
        if (favoriteRepo.existsByUserEmailAndDishId(email, dishId)) {
            return ResponseEntity.badRequest().body("Already in favorites");
        }

        Favorite fav = new Favorite(
                email,
                dishId,
                (String) payload.get("dishName"),
                (String) payload.get("dishImage"),
                Double.parseDouble(payload.get("dishPrice").toString()),
                (String) payload.get("chefName")
        );

        favoriteRepo.save(fav);
        return ResponseEntity.ok("Added to favorites");
    }

    @Transactional
    @PostMapping("/remove-favorite")
    public ResponseEntity<?> removeFavorite(@RequestBody Map<String, String> payload, HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        List<Object> auth = authUtil.validatetoken(token);
        String email = (String) auth.get(0);

        String dishId = payload.get("dishId");
        favoriteRepo.deleteByUserEmailAndDishId(email, dishId);
        
        return ResponseEntity.ok("Removed from favorites");
    }

    @GetMapping("/get-favorites")
    public ResponseEntity<List<Favorite>> getFavorites(HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        List<Object> auth = authUtil.validatetoken(token);
        String email = (String) auth.get(0);

        return ResponseEntity.ok(favoriteRepo.findByUserEmail(email));
    }
}

package com.example.Gruhani.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FeedBackDto {
    @NotNull
    Long orderId;
    @NotNull
    Long sellerId;
    int rating;
    String comment;

}

package com.example.demo.siteuser.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private String email;
    private int amount;
}

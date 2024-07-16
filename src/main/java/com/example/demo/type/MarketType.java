package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MarketType {
    REGULAR(0, "상설장"),
    FIVE_DAY(1, "오일장");

    private int value;
    private String name;
}

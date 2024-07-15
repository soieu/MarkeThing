package com.example.demo.csv.controller;

import com.example.demo.csv.service.CsvService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * springboot 실행 후
 * readCsv api를 호출해줘야 DB에 Market 정보가 저장됩니다.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CsvController {
    private final CsvService csvservice;
    @GetMapping(value = "get", produces = "application/json")
    public void readCsv() {
        csvservice.saveMarkets();
    }
}

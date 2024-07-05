package com.example.demo.csv.controller;

import com.example.demo.csv.service.CsvService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CsvController {
    private final CsvService csvservice;
    @GetMapping(value = "get",produces = "application/json")
    public void readCsv(){
        csvservice.saveMarkets();
    }

}

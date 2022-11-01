package com.ds.dssearcher.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
public class FeedController {
    @Value("${appconfig.host}")
    private String host;
    @GetMapping("/hello")
    private ResponseEntity<String> getHello(){
        return ResponseEntity.ok("hello host: " + host);
    }
}

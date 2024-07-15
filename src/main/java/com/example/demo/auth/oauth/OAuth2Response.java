package com.example.demo.auth.oauth;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();


}

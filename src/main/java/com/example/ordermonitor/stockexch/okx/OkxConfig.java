package com.example.ordermonitor.stockexch.okx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkxConfig {

    private final String apiKey;
    private final String secretKey;
    private final String passphrase;


    public OkxConfig(@Value("${okx.api.key}") String apiKey,
                     @Value("${okx.api.secretkey}") String secretKey,
                     @Value("${okx.api.passphrase}") String passphrase) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

}

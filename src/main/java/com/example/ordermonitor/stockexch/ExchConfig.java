package com.example.ordermonitor.stockexch;

public class ExchConfig {

    private final String apiKey;
    private final String secretKey;
    private final String passphrase;

    public ExchConfig(String apiKey,
                      String secretKey,
                      String passphrase) {
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

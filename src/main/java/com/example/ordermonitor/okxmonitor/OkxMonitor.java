package com.example.ordermonitor.okxmonitor;

import com.example.ordermonitor.okxmonitor.config.OkxConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Component
public class OkxMonitor {

    private final OkxConfig okxConfig;

    private final RestClient restClient;

    public OkxMonitor(OkxConfig okxConfig) {
        restClient = RestClient.create();
        this.okxConfig = okxConfig;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedDelayTask() {
        String host = "https://www.okx.com";
        String url = "/api/v5/trade/orders-pending";
        String timestampStr = getUnixTime();
        String strForSign = timestampStr + "GET" + url;
        String headerSign = null;

        try {
            byte[] hmacEncoded = encodeHmac256(strForSign.getBytes(StandardCharsets.UTF_8),
                    okxConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
            headerSign = new String(Base64.getEncoder().encode(hmacEncoded));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (InvalidKeyException e) {
            System.out.println(e);
        }
        RestClient.RequestHeadersSpec<?> request = restClient.get()
                .uri(host + url)
                .header("OK-ACCESS-KEY",okxConfig.getApiKey())
                .header("OK-ACCESS-SIGN",headerSign)
                .header("OK-ACCESS-TIMESTAMP", timestampStr)
                .header("OK-ACCESS-PASSPHRASE", okxConfig.getPassphrase());
        RestClient.ResponseSpec responseSpec = request.retrieve();
        System.out.println("response = " + responseSpec.body(String.class));
    }

    private static byte[] encodeHmac256(byte[] message, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String hmacSha256_Algo = "HmacSHA256";
        Mac sha256_HMAC = Mac.getInstance(hmacSha256_Algo);
        sha256_HMAC.init(new SecretKeySpec(key, hmacSha256_Algo));
        return sha256_HMAC.doFinal(message);
    }

    /**
     * UNIX timestamp ISO 8601 rule eg: 2018-02-03T05:34:14.110Z
     */
    private static String getUnixTime() {
        String now = Instant.now().toString();
        // Instant.now().toString() в windows и linux могут давать разные результаты,
        // поэтому приводим к нужному нам текстовому формату принудительно
        int dotIndex = now.lastIndexOf(".");
        return now.substring(0, dotIndex)
                + now.substring(dotIndex).substring(0,4)
                + now.substring(now.length()-1);
    }

}

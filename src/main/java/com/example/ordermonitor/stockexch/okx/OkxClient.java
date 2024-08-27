package com.example.ordermonitor.stockexch.okx;

import com.example.ordermonitor.dto.SEActiveOrderReceiptWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OkxClient {

    private final String HOST = "https://www.okx.com";

    private final RestClient restClient;
    private final OkxConfig okxConfig;

    public OkxClient(OkxConfig okxConfig) {
        restClient = RestClient.create();
        this.okxConfig = okxConfig;
    }

    public List<SEActiveOrderReceiptWrapper> requestExchangeOrders() {
        String url = "/api/v5/trade/orders-pending" + "?" + "instType=SPOT" + "&" + "ordType=limit";
        String timestampStr = OkxClient.getUnixTime();
        String strForSign = timestampStr + "GET" + url;
        String headerSign = null;

        try {
            byte[] hmacEncoded = OkxClient.encodeHmac256(strForSign.getBytes(StandardCharsets.UTF_8),
                    okxConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
            headerSign = new String(Base64.getEncoder().encode(hmacEncoded));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (InvalidKeyException e) {
            System.out.println(e);
        }
        RestClient.RequestHeadersSpec<?> request = restClient.get()
                .uri(HOST + url)
                .header("OK-ACCESS-KEY",okxConfig.getApiKey())
                .header("OK-ACCESS-SIGN",headerSign)
                .header("OK-ACCESS-TIMESTAMP", timestampStr)
                .header("OK-ACCESS-PASSPHRASE", okxConfig.getPassphrase());
        String responseJson = request.retrieve().body(String.class);
        System.out.println("response = " + responseJson);

        ObjectMapper mapper = new ObjectMapper();
        List<SEActiveOrderReceiptWrapper> seOrderList = null;
        try {
            JsonNode jsonNode = mapper.readTree(responseJson);
            seOrderList = mapper.readValue(jsonNode.get("data").toString(), new TypeReference<>(){});
            System.out.println("seOrderList = " + seOrderList.size());
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return seOrderList == null ? new ArrayList<>() : seOrderList;
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

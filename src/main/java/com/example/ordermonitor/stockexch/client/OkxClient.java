package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.order.ExchOrderWrapper;
import com.example.ordermonitor.stockexch.ExchConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class OkxClient extends AbstractExchClient implements ExchClient {

    private final String HOST = "https://www.okx.com";

    private final ObjectMapper jsonMapper = new ObjectMapper();

    public OkxClient(ExchConfig config) {
        this.config = config;
        restClient = RestClient.create();
    }

    public List<ExchOrderWrapper> requestOrderList() {
        String url = "/api/v5/trade/orders-pending" + "?" + "instType=SPOT" + "&" + "ordType=limit";
        String responseJson = encodeAndRequest(url);
        List<ExchOrderWrapper> seOrderList = null;
        try {
            JsonNode jsonNode = jsonMapper.readTree(responseJson);
            seOrderList = jsonMapper.readValue(jsonNode.get("data").toString(), new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return seOrderList == null ? new ArrayList<>() : seOrderList;
    }

    private String encodeAndRequest(String url) {
        String timestampStr = getUnixTime();
        String strForSign = timestampStr + "GET" + url;
        String headerSign = null;
        try {
            byte[] hmacEncoded = encodeHmac256(strForSign.getBytes(StandardCharsets.UTF_8),
                    config.getSecretKey().getBytes(StandardCharsets.UTF_8));
            headerSign = new String(Base64.getEncoder().encode(hmacEncoded));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (InvalidKeyException e) {
            System.out.println(e);
        }
        RestClient.RequestHeadersSpec<?> request = restClient.get()
                .uri(HOST + url)
                .header("OK-ACCESS-KEY",config.getApiKey())
                .header("OK-ACCESS-SIGN",headerSign)
                .header("OK-ACCESS-TIMESTAMP", timestampStr)
                .header("OK-ACCESS-PASSPHRASE", config.getPassphrase());
        return request.retrieve().body(String.class);
    }

    public ExchOrderWrapper requestOrderDetails(String instId, String seOrderId) {
        String url = "/api/v5/trade/order" + "?" + "instId=" + instId + "&" + "ordId=" + seOrderId;
        String responseJson = encodeAndRequest(url);
        List<ExchOrderWrapper> exchOrderWrapperList = null;
        try {
            JsonNode jsonNode = jsonMapper.readTree(responseJson);
            exchOrderWrapperList = jsonMapper.readValue(jsonNode.get("data").toString(), new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return exchOrderWrapperList == null ? null : exchOrderWrapperList.get(0);
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

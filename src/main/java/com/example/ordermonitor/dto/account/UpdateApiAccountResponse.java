package com.example.ordermonitor.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApiAccountResponse {

    private Integer responseCode;
    private String responseMessage;
    private ApiAccountRestWrapper account;

    public UpdateApiAccountResponse(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.account = null;
    }

}

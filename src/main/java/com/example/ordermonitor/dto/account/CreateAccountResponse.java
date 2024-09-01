package com.example.ordermonitor.dto.account;

import com.example.ordermonitor.model.ApiAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {

    private Integer responseCode;
    private String responseMessage;
    private ApiAccount account;

}

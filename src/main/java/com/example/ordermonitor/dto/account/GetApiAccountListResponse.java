package com.example.ordermonitor.dto.account;

import com.example.ordermonitor.model.ApiAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetApiAccountListResponse {

    private Integer responseCode;
    private String responseMessage;
    private List<ApiAccount> accountList;

    public GetApiAccountListResponse(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountList = null;
    }

}

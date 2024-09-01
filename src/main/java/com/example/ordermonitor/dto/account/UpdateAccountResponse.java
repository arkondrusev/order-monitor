package com.example.ordermonitor.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountResponse {

    private Integer responseCode;
    private String responseMessage;
    private AccountWrapper account;

}

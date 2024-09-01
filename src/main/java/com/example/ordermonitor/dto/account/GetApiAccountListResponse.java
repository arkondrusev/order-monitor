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

    private List<ApiAccount> accountList;

}

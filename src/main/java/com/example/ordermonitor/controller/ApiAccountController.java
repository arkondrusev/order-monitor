package com.example.ordermonitor.controller;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.service.ApiAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("account")
@RestController
@RequiredArgsConstructor
public class ApiAccountController {

    private final ApiAccountService accountService;

    @PostMapping("create")
    public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("get-list")
    public GetAccountListResponse getAccountList(@RequestBody GetAccountListRequest request) {
        return accountService.getAccountList(request);
    }

    @PutMapping("update")
    public UpdateAccountResponse updateAccount(@RequestBody UpdateAccountRequest request) {
        return accountService.updateAccount(request);
    }

    @DeleteMapping("delete")
    public DeleteAccountResponse deleteAccount(@RequestBody DeleteAccountRequest request) {
        return accountService.deleteAccount(request);
    }

}

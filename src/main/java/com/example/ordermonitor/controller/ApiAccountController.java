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
    public CreateApiAccountResponse createAccount(@RequestBody CreateApiAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("get-list")
    public GetApiAccountListResponse getAccountList(@RequestBody GetApiAccountListRequest request) {
        return accountService.getAccountList(request);
    }

    @PutMapping("update")
    public UpdateApiAccountResponse updateAccount(@RequestBody UpdateApiAccountRequest request) {
        return accountService.updateAccount(request);
    }

    @DeleteMapping("delete")
    public DeleteApiAccountResponse deleteAccount(@RequestBody DeleteApiAccountRequest request) {
        return accountService.deleteAccount(request);
    }

}

package com.dogukan.takehometwo.controller;

import com.dogukan.takehometwo.dto.request.ApiResponse;
import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.dto.request.RequestTransferMoney;
import com.dogukan.takehometwo.dto.request.ResponseBalance;
import com.dogukan.takehometwo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
public class RestAccountController {
    private final AccountService accountService;

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody RequestCreateAccount account) {
        accountService.createAccount(account);
        return ResponseEntity.ok(new ApiResponse(true, "Account created successfully."));
    }

    @GetMapping(path = "/balance")
    public ResponseEntity<ResponseBalance> balance(@RequestParam("accountNumber") String accountNumber) {
        ResponseBalance balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @PostMapping(path = "/transfer")
    public ResponseEntity<ApiResponse> transferMoney(@RequestBody RequestTransferMoney transferMoney) {
        accountService.transferMoney(transferMoney);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully transfered money between accounts."));
    }
}

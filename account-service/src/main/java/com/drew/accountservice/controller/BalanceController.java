package com.drew.accountservice.controller;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceHistoryDto;
import com.drew.accountservice.entity.Balance;
import com.drew.accountservice.exception.AccountNotFoundException;
import com.drew.accountservice.exception.InvalidAllocationException;
import com.drew.accountservice.service.BalanceService;
import com.drew.commonlibrary.dto.KafkaBalanceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping
    @Operation(
            summary = "Add New Account Balance",
            description = "Updates the balance of an account with the specified ID if it belongs to the logged-in user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance updated successfully", content = @Content(schema = @Schema(implementation = KafkaBalanceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request - the provided data is invalid or incomplete"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to update this account"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<?> addNewBalance(@RequestHeader("X-User-ID") String keycloakUserId,
                                           @PathVariable Long accountId,
                                           @RequestBody BalanceAllocationDto balanceAllocationDto) {
        try {
            Balance newBalance = balanceService.addNewBalance(accountId, keycloakUserId, balanceAllocationDto);
            return ResponseEntity.ok(newBalance);
        } catch (AccountNotFoundException | InvalidAllocationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(
            summary = "Get Balance History",
            description = "Retrieves the history of balances for a given account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance history retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to view this account's balance history"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<?> getBalanceHistory(@RequestHeader("X-User-ID") String keycloakUserId,
                                               @PathVariable Long accountId) {
        try {
            BalanceHistoryDto balanceHistory = balanceService.getBalanceHistory(keycloakUserId, accountId);
            return ResponseEntity.ok(balanceHistory);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/balanceId")
    @Operation(
            summary = "Get Balance By ID",
            description = "Retrieves the specified balance entry for a given account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance entry retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to view this account's balance history"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account or balance entry does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<?> getBalanceById(@RequestHeader("X-User-ID") String keycloakUserId,
                                               @PathVariable Long accountId,
                                               @PathVariable Long balanceId) {
        try {
            BalanceDto balance = balanceService.getBalanceById(keycloakUserId, accountId, balanceId);
            return ResponseEntity.ok(balance);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

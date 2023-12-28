package com.drew.accountservice.controller;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountUpdateDto;
import com.drew.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(
            summary = "Register a new account",
            description = "Creates a new account for logged in user with the provided information." +
                    "The account's unique identifier is returned in the Location header of the response.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = AccountOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request - the provided data is invalid or incomplete"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to create a new account"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<AccountOutputDto> createAccount(@RequestHeader("X-User-ID") String keycloakUserId, @RequestBody AccountInputDto accountInputDto, UriComponentsBuilder uriBuilder) {
        AccountOutputDto accountOutputDto = accountService.createAccount(keycloakUserId, accountInputDto);

        URI location = uriBuilder
                .path("/{id}")
                .buildAndExpand(accountOutputDto.accountId())
                .toUri();

        return ResponseEntity.created(location).body(accountOutputDto);
    }

    @GetMapping
    @Operation(
            summary = "Get User Accounts",
            description = "Retrieves all accounts associated with the logged-in user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content = @Content(schema = @Schema(implementation = AccountOutputDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to view the accounts"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<List<AccountOutputDto>> getUserAccounts(@RequestHeader("X-User-ID") String keycloakUserId) {
        List<AccountOutputDto> accounts = accountService.getUserAccounts(keycloakUserId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    @Operation(
            summary = "Get Account by ID",
            description = "Retrieves the account with the specified ID if it belongs to the logged-in user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully", content = @Content(schema = @Schema(implementation = AccountOutputDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to view this account"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<AccountOutputDto> getAccountById(@RequestHeader("X-User-ID") String keycloakUserId, @PathVariable Long accountId) {
        Optional<AccountOutputDto> accountOutputDto = accountService.getAccountByIdAndKeycloakId(accountId, keycloakUserId);

        return accountOutputDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{accountId}")
    @Operation(
            summary = "Update Account by ID",
            description = "Updates the account with the specified ID if it belongs to the logged-in user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(schema = @Schema(implementation = AccountOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request - the provided data is invalid or incomplete"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to update this account"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<AccountOutputDto> updateAccountById(@RequestHeader("X-User-ID") String keycloakUserId,
                                                              @PathVariable Long accountId,
                                                              @RequestBody AccountUpdateDto accountUpdateDto) {
        Optional<AccountOutputDto> updatedAccount = accountService.updateAccountByIdAndKeycloakId(accountId, keycloakUserId, accountUpdateDto);

        return updatedAccount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{accountId}")
    @Operation(
            summary = "Soft Delete Account by ID",
            description = "Archives the account with the specified ID if it belongs to the logged-in user, marking it as inactive."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account archived successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to archive this account"),
            @ApiResponse(responseCode = "404", description = "Not Found - the account does not exist or does not belong to the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<Void> softDeleteAccount(@RequestHeader("X-User-ID") String keycloakUserId, @PathVariable Long accountId) {
        boolean isDeleted = accountService.softDeleteAccount(accountId, keycloakUserId);
        return isDeleted ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

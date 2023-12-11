package com.drew.accountservice.controller;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
                .buildAndExpand(accountOutputDto.getAccountId())
                .toUri();

        return ResponseEntity.created(location).body(accountOutputDto);
    }


}

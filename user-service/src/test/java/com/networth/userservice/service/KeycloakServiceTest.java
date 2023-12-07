package com.networth.userservice.service;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.PasswordCredentialDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.dto.UserRepresentationDto;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.feign.KeycloakFormClient;
import com.networth.userservice.util.KeycloakFormDataBuilder;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeycloakServiceTest {
    @Mock
    private KeycloakFormClient keycloakFormClient;
    @Mock
    private KeycloakFormDataBuilder keycloakFormDataBuilder;
    @Mock
    private KeycloakClient keycloakClient;
    @InjectMocks
    private KeycloakService keycloakService;

    private KeycloakAccessDto mockKeycloakAccessDto;
    private TokenResponse mockTokenResponse;
    private LoginDto mockLoginDto;
    private RegisterDto mockRegisterDto;
    private Response mockResponse;
    private User mockUser;
    private UserRepresentationDto mockUserRepresentationDto;
    private PasswordCredentialDto mockPasswordCredentialDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockKeycloakAccessDto = new KeycloakAccessDto();
        mockKeycloakAccessDto.setRefreshToken("mockRefreshToken");
        mockKeycloakAccessDto.setScope("mockScope");
        mockKeycloakAccessDto.setClientId("mockClientId");
        mockKeycloakAccessDto.setUsername("mockUsername");
        mockKeycloakAccessDto.setPassword("mockPassword");
        mockKeycloakAccessDto.setIdTokenHint("mockIdTokenHint");
        mockKeycloakAccessDto.setClientSecret("mockClientSecret");
        mockKeycloakAccessDto.setGrantType("mockGrantType");
        mockKeycloakAccessDto.setPostLogoutRedirectUri("mockRedirectUri");
        mockKeycloakAccessDto.setToken("mockToken");

        mockLoginDto = new LoginDto();
        mockLoginDto.setUsername("seeduser");
        mockLoginDto.setPassword("Password123!");

        mockTokenResponse = new TokenResponse();
        mockTokenResponse.setAccessToken("mockAccessToken");
        mockTokenResponse.setExpiresIn(300);
        mockTokenResponse.setRefreshExpiresIn(300);
        mockTokenResponse.setRefreshToken("mockRefreshToken");
        mockTokenResponse.setTokenType("mockTokenType");
        mockTokenResponse.setScope("mockScope");
        mockTokenResponse.setIdToken("mockIdToken");
        mockTokenResponse.setSessionState("mockSessionState");
        mockTokenResponse.setNotBeforePolicy(1);

        mockRegisterDto = new RegisterDto();
        mockRegisterDto.setEmail("mockEmail");
        mockRegisterDto.setPassword("Password123!");
        mockRegisterDto.setUsername("seeduser");

        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setKeycloakId("0c77e0c4-95f4-4704-a24f-ee22deb43609");
        mockUser.setUsername("seeduser");
        mockUser.setEmail("seeduser@example.co.uk");
        mockUser.setActiveUser(true);
        mockUser.setTaxRate(null);
        mockUser.setDateOfBirth(null);
        mockUser.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUser.setDateUpdated(null);

        mockUserRepresentationDto = new UserRepresentationDto();
        mockUserRepresentationDto.setEmail("seeduser@example.co.uk");
        mockUserRepresentationDto.setCredentials((List<PasswordCredentialDto>) mockPasswordCredentialDto);
        mockUserRepresentationDto.setUsername("seeduser");
        mockUserRepresentationDto.setEnabled(true);

        mockPasswordCredentialDto = new PasswordCredentialDto();
        mockPasswordCredentialDto.setValue("mockValue");
        mockPasswordCredentialDto.setTemporary(false);
        mockPasswordCredentialDto.setType("mockType");

        when(keycloakFormClient.getAdminAccessToken(any(KeycloakAccessDto.class)))
                .thenReturn(createMockAdminTokenResponse());
    }

    private Response createMockResponse(int status) {
        return Response.builder()
                .status(status)
                .request(Request.create(Request.HttpMethod.POST, "", Collections.emptyMap(), null, Charset.defaultCharset(), null))
                .build();
    }

    private TokenResponse createMockAdminTokenResponse() {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("mock-admin-access-token");
        return tokenResponse;
    }


    @Test
    public void testGetAdminAccessTokenSuccess() {
        TokenResponse mockTokenResponse = new TokenResponse();
        mockTokenResponse.setAccessToken("admin-access-token");

        when(keycloakFormDataBuilder.buildAdminAccessFormData()).thenReturn(mockKeycloakAccessDto);
        when(keycloakFormClient.getAdminAccessToken(mockKeycloakAccessDto)).thenReturn(mockTokenResponse);

        String accessToken = keycloakService.getAdminAccessToken();

        assertNotNull(accessToken);
        assertEquals("admin-access-token", accessToken);
        verify(keycloakFormClient).getAdminAccessToken(mockKeycloakAccessDto);
    }

    @Test
    public void testGetAdminAccessTokenFeignException() {
        when(keycloakFormDataBuilder.buildAdminAccessFormData()).thenReturn(new KeycloakAccessDto());
        when(keycloakFormClient.getAdminAccessToken(any(KeycloakAccessDto.class))).thenThrow(FeignException.class);

        assertThrows(AuthenticationServiceException.class, () -> keycloakService.getAdminAccessToken());

        verify(keycloakFormClient).getAdminAccessToken(any(KeycloakAccessDto.class));
    }

    @Test
    public void testGetAdminAccessTokenGeneralException() {
        when(keycloakFormDataBuilder.buildAdminAccessFormData()).thenReturn(new KeycloakAccessDto());
        when(keycloakFormClient.getAdminAccessToken(any(KeycloakAccessDto.class))).thenThrow(RuntimeException.class);

        assertThrows(AuthenticationServiceException.class, () -> keycloakService.getAdminAccessToken());

        verify(keycloakFormClient).getAdminAccessToken(any(KeycloakAccessDto.class));
    }

    @Test
    public void testGetUserAccessTokenSuccess() {
        when(keycloakFormDataBuilder.buildUserAccessFormData(mockLoginDto)).thenReturn(mockKeycloakAccessDto);
        when(keycloakFormClient.getUserAccessToken(mockKeycloakAccessDto)).thenReturn(mockTokenResponse);

        TokenResponse tokenResponse = keycloakService.getUserAccessToken(mockLoginDto);

        assertNotNull(tokenResponse);
        assertEquals(mockTokenResponse, tokenResponse);
        verify(keycloakFormClient).getUserAccessToken(mockKeycloakAccessDto);
    }

    @Test
    public void testGetUserAccessTokenFeignException() {
        when(keycloakFormDataBuilder.buildUserAccessFormData(mockLoginDto)).thenReturn(mockKeycloakAccessDto);
        when(keycloakFormClient.getUserAccessToken(mockKeycloakAccessDto)).thenThrow(FeignException.class);

        assertThrows(AuthenticationServiceException.class, () -> keycloakService.getUserAccessToken(mockLoginDto));

        verify(keycloakFormClient).getUserAccessToken(mockKeycloakAccessDto);
    }

    @Test
    public void testGetUserAccessTokenGeneralException() {
        when(keycloakFormDataBuilder.buildUserAccessFormData(mockLoginDto)).thenReturn(mockKeycloakAccessDto);
        when(keycloakFormClient.getUserAccessToken(mockKeycloakAccessDto)).thenThrow(RuntimeException.class);

        assertThrows(AuthenticationServiceException.class, () -> keycloakService.getUserAccessToken(mockLoginDto));

        verify(keycloakFormClient).getUserAccessToken(mockKeycloakAccessDto);
    }

    @Test
    public void testCreateUserSuccess() {
        mockResponse = createMockResponse(HttpStatus.CREATED.value());
        when(keycloakFormDataBuilder.createUserRepresentation(mockRegisterDto)).thenReturn(mockUserRepresentationDto);
        when(keycloakClient.createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto))).thenReturn(mockResponse);

        Response response = keycloakService.createUser(mockRegisterDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.status());
        verify(keycloakClient).createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto));
    }

    @Test
    public void testCreateUserFeignException() {
        Response feignResponse = createMockResponse(HttpStatus.BAD_REQUEST.value());
        FeignException feignException = FeignException.errorStatus("createKeycloakUser", feignResponse);
        when(keycloakFormDataBuilder.createUserRepresentation(mockRegisterDto)).thenReturn(mockUserRepresentationDto);
        when(keycloakClient.createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto))).thenThrow(feignException);

        assertThrows(AuthenticationServiceException.class, () -> keycloakService.createUser(mockRegisterDto));

        verify(keycloakClient).createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto));
    }

    @Test
    public void testCreateUserGeneralException() {
        when(keycloakFormDataBuilder.createUserRepresentation(mockRegisterDto)).thenReturn(mockUserRepresentationDto);
        when(keycloakClient.createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto))).thenThrow(RuntimeException.class);

        assertThrows(UserServiceException.class, () -> keycloakService.createUser(mockRegisterDto));

        verify(keycloakClient).createKeycloakUser(any(Map.class), eq(mockUserRepresentationDto));
    }
}

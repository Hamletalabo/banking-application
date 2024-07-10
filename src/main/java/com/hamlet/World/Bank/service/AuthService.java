package com.hamlet.World.Bank.service;

import com.hamlet.World.Bank.payload.request.LoginRequest;
import com.hamlet.World.Bank.payload.request.UserRequest;
import com.hamlet.World.Bank.payload.response.ApiResponse;
import com.hamlet.World.Bank.payload.response.BankResponse;
import com.hamlet.World.Bank.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest);
}

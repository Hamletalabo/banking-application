package com.hamlet.World.Bank.service;

import com.hamlet.World.Bank.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {
    ResponseEntity<BankResponse<String>>uploadProfilePic(Long id, MultipartFile multipartFile);
}

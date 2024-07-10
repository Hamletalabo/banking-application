package com.hamlet.World.Bank.infrastructure.controller;

import com.hamlet.World.Bank.payload.response.BankResponse;
import com.hamlet.World.Bank.service.GeneralUserService;
import com.hamlet.World.Bank.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class GeneralUserController {
    private final GeneralUserService generalUserService;

    @PutMapping("/{id}/profile-pics")
    public ResponseEntity<BankResponse<String>> profileUpload(
            @PathVariable("id")Long id, @RequestParam MultipartFile profilePic){
        if (profilePic.getSize()> AppConstants.MAX_FILE_SIZE){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BankResponse<>(
                            "File size exceed the required limit"
                    ));
        }
        return generalUserService.uploadProfilePic(id, profilePic);
    }
}

package com.hamlet.World.Bank.service.Impl;

import com.hamlet.World.Bank.domain.entity.UserEntity;
import com.hamlet.World.Bank.domain.enums.Role;
import com.hamlet.World.Bank.payload.request.EmailDetails;
import com.hamlet.World.Bank.payload.request.LoginRequest;
import com.hamlet.World.Bank.payload.request.UserRequest;
import com.hamlet.World.Bank.payload.response.AccountInfo;
import com.hamlet.World.Bank.payload.response.ApiResponse;
import com.hamlet.World.Bank.payload.response.BankResponse;
import com.hamlet.World.Bank.payload.response.JwtAuthResponse;
import com.hamlet.World.Bank.repository.UserRepository;
import com.hamlet.World.Bank.service.AuthService;
import com.hamlet.World.Bank.service.EmailService;
import com.hamlet.World.Bank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public BankResponse registerUser(UserRequest userRequest) {
            if(userRepository.existsByEmail(userRequest.getEmail())) {
                BankResponse response = BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                        .build();

                return response;
            }
        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber())
                .bankName("Hamlet Bank")
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .BVN(userRequest.getBVN())
                .pin(userRequest.getPin())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .role(Role.USER)
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .build();

            UserEntity savedUser = userRepository.save(newUser);

            //add email alert here
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("CONGRATULATION!!! Your account has been successfully created. "
                        + "\n Your Account Details: \n " + "Account Name : "
                        + savedUser.getFirstName() + " "
                        + savedUser.getLastName() + " "
                        + savedUser.getOtherName() + "\n Account Number : "
                        + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .bankName(savedUser.getBankName())
                        .accountName(savedUser.getFirstName() + " " +
                                savedUser.getLastName() + " " +
                                savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(loginRequest.getEmail());

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You are logged in ")
                .recipient(loginRequest.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, contact support desk.")
                .build();

        emailService.sendEmailAlert(loginAlert);

        UserEntity user = userEntityOptional.get();

        return ResponseEntity.status(HttpStatus.OK)
                .body( new ApiResponse<>(
                                "Login Successfully",
                                JwtAuthResponse.builder()
                                        .accessToken("generate token here")
                                        .tokenType("Bearer")
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .gender(user.getGender())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .profilePicture(user.getProfilePicture())
                                        .build()
                        )
                );
    }
}

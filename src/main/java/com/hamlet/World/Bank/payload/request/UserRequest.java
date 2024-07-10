package com.hamlet.World.Bank.payload.request;

import com.hamlet.World.Bank.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 125, message = "First name must be at least 2 and maximum of 125")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 125, message = "Last name must be at least 2 and maximum of 125")
    private String lastName;
    private String otherName;
    private String address;
    private String gender;
    private String stateOfOrigin;
    private String phoneNumber;
    private String BVN;
    private String pin;
    private String email;
    private String password;

}

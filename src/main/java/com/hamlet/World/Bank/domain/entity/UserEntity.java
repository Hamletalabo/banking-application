package com.hamlet.World.Bank.domain.entity;

import com.hamlet.World.Bank.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseClass{

    private String firstName;
    private String lastName;
    private String otherName;
    private String address;
    private String gender;
    private String stateOfOrigin;
    private String phoneNumber;
    private String accountNumber;
    private String bankName;
    private BigDecimal accountBalance;
    private String profilePicture;
    private String status;
    private String BVN;
    private String pin;
    private String email;
    private String password;
    private Role role;

}

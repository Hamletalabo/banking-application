package com.hamlet.World.Bank.service.Impl;

import com.hamlet.World.Bank.domain.entity.UserEntity;
import com.hamlet.World.Bank.payload.request.CreditAndDebitRequest;
import com.hamlet.World.Bank.payload.request.EmailDetails;
import com.hamlet.World.Bank.payload.request.EnquiryRequest;
import com.hamlet.World.Bank.payload.request.TransferRequest;
import com.hamlet.World.Bank.payload.response.AccountInfo;
import com.hamlet.World.Bank.payload.response.BankResponse;
import com.hamlet.World.Bank.repository.UserRepository;
import com.hamlet.World.Bank.service.EmailService;
import com.hamlet.World.Bank.service.UserService;
import com.hamlet.World.Bank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.hamlet.World.Bank.utils.AccountUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {

        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }//else statement

        UserEntity foundUserAccount = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(ACCOUNT_EXISTS_CODE)
                .responseMessage(ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUserAccount.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUserAccount.getFirstName() + " "
                        + foundUserAccount.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountNameExists = userRepository.existsByAccountNumber(
                enquiryRequest.getAccountNumber());

        if (!isAccountNameExists){
            return ACCOUNT_NUMBER_NON_EXISTS_MESSAGE;
        }// else statement

        UserEntity foundUserAccount = userRepository.findByAccountNumber(
                enquiryRequest.getAccountNumber());


        return foundUserAccount.getLastName() + " " +foundUserAccount.getLastName()
                + " " + foundUserAccount.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest) {

        boolean isAccountExists = userRepository.existsByAccountNumber(
                creditAndDebitRequest.getAccountNumber());

        //check is the account number exists
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }
        //if the details of the user exists in this case the account number which should generate the account name
        UserEntity userToCredit = userRepository.findByAccountNumber(
                creditAndDebitRequest.getAccountNumber());

        userToCredit.setAccountBalance(userToCredit.getAccountBalance()
                .add(creditAndDebitRequest.getAmount()));

        userRepository.save(userToCredit);

        //send a credit alert to the user
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("You account has been credited with " +
                        creditAndDebitRequest.getAmount()+ " from" +
                        userToCredit.getFirstName() + " " + userToCredit.getLastName()
                        + " Your current account balance is "+
                        userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseCode(ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+ " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest creditAndDebitRequest) {

        boolean isAccountExists = userRepository.existsByAccountNumber(
                creditAndDebitRequest.getAccountNumber());

        //check is the account number exists
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(
                creditAndDebitRequest.getAccountNumber());

        //check for insufficient balance
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditAndDebitRequest.getAmount().toBigInteger();
        if (availableBalance.intValue()< debitAmount.intValue()){

            return BankResponse.builder()
                    .responseCode("006")
                    .responseMessage("Insufficient Balance")
                    .accountInfo(null)
                    .build();


        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance()
                    .subtract(creditAndDebitRequest.getAmount()));

            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of "+ creditAndDebitRequest.getAmount() +
                            " has been debited from your account! your current account balance is "
                    + userToDebit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);
        }


        return BankResponse.builder()
                .responseCode("007")
                .responseMessage("Account debited successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public BankResponse transferMoney(TransferRequest transferRequest) {
        // Check if the source and destination account numbers are the same
        if (transferRequest.getSourceAccountNo().equals(transferRequest.getDestinationAccountNo())) {
            return BankResponse.builder()
                    .responseCode("010")  // Use an appropriate response code
                    .responseMessage("Cannot transfer money to the same account")
                    .accountInfo(null)
                    .build();
        }

        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(
                transferRequest.getDestinationAccountNo());

        //check is the account number exists
        if (!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNo());

        if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
            return BankResponse.builder()
                    .responseCode("009")
                    .responseMessage("INSUFFICIENT BALANCE")
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccountUser);

        String sourceUserName = sourceAccountUser.getFirstName() + " " +
                sourceAccountUser.getLastName()+ " " +sourceAccountUser.getOtherName();


        EmailDetails debitAlert= EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of "
                + transferRequest.getAmount()
                +" has been deducted from your account. Your current account balance is "
                +sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        UserEntity destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNo());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ACCOUNT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your account has been credited with "
                + transferRequest.getAmount()+ " from " + sourceUserName + "Your current account balance is  "
                + destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        return   BankResponse.builder()
                .responseCode("200")
                .responseMessage("Transfer successfully")
                .accountInfo(null)
                .build();
    }
}

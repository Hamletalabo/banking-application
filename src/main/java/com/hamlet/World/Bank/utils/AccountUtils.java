package com.hamlet.World.Bank.utils;

import java.time.Year;

public class AccountUtils {
    //create static fields that we can call anywhere
    // this class is a util class, we can use the fields everywhere

    public static final String ACCOUNT_EXISTS_CODE= "001";

    public static final String ACCOUNT_EXISTS_MESSAGE= "This user already has an account created!";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";

    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been created successfully!";

    public static final String ACCOUNT_NUMBER_NON_EXISTS_CODE= "003";

    public static final String ACCOUNT_NUMBER_NON_EXISTS_MESSAGE= "Provided account number does not exists";

    public static final String ACCOUNT_NUMBER_FOUND_CODE= "004";

    public static final String ACCOUNT_NUMBER_FOUND_MESSAGE= "Account number found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE= "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE= "You account has been credited successfully";



    public static String generateAccountNumber(){
        //this algorithm assumes that the account no will be a total of 10 digits, following the
        //convention in Nigeria
        // 1. get the current year, to give us the first 4 digits.
        Year currentYear = Year.now();
        //2. get the other 6 random digits

        int min = 100000;
        int max = 999999;

        // generate a random number btw min and max

        int randomNumber = (int)Math.floor(Math.random()* (max - min + 1) + min);

        //convert current year and random 6 number to string and then concatenate them
        String year = String.valueOf(currentYear);
        String randomNum = String.valueOf(randomNumber);
        //append both year and random number to generate the 10 digits account number using StringBuilder

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNum).toString();
    }
}

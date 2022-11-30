package com.example.assessment.UtilityFunctions;

import java.time.LocalDate;

public class GeneralUtilities {

    public static boolean dateInFuture(LocalDate dateToCheck) {
        LocalDate todayDate = LocalDate.now();
        if(dateToCheck.equals(todayDate) || dateToCheck.isAfter(todayDate)) {
            return true;
        }
        return false;
    };
}

package com.example.assessment.UtilityFunctions;

import ch.qos.logback.core.util.IncompatibleClassException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class GeneralUtilitiesTest {

    @Test
    void test_futureDate_whereDateIsToday() {
        LocalDate d = LocalDate.now();
        Boolean isFutureDate = GeneralUtilities.dateInFuture(d);
        assertEquals(true, isFutureDate);
    }

    @Test
    void test_futureDate_whereDateIsInFuture() {
        LocalDate d = LocalDate.now().plusDays(60);
        Boolean isFutureDate = GeneralUtilities.dateInFuture(d);
        assertEquals(true, isFutureDate);
    }

    @Test
    void test_futureDate_whereDateIsInPast() {
        LocalDate d = LocalDate.now().minusDays(30);
        Boolean isFutureDate = GeneralUtilities.dateInFuture(d);
        assertEquals(false, isFutureDate);
    }

    @Test
    void test_FutureDate_whereDateIsNotADate() {
        assertThrows(DateTimeParseException.class, () -> {
            LocalDate d = LocalDate.parse("2022-aa-aa");
            GeneralUtilities.dateInFuture(d);
        });
        assertThrows(DateTimeParseException.class, () -> {
            LocalDate d = LocalDate.parse("_");
            GeneralUtilities.dateInFuture(d);
        });
    }
}
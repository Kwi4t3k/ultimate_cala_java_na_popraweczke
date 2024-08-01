package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(String message) {
        super(message);
    }
}
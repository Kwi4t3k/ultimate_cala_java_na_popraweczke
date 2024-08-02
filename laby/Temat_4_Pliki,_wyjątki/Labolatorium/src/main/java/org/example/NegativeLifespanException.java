package org.example;

public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(String message) {
        super(message);
    }
}
package org.example;

public class NotEnoughCreditsException extends Exception{
    public NotEnoughCreditsException(String message){
        super(message);
    }
}

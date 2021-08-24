package com.beto.e;

public class NotAbleToMoveException extends Exception {

    public NotAbleToMoveException() {
        super("No es posible mover el valor dado");
    }

}

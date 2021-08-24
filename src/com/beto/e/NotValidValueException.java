package com.beto.e;

public class NotValidValueException extends Exception {

    public NotValidValueException() {
        super("El valor dado no es valido");
    }

}

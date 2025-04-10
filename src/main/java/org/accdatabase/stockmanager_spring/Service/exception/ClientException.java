package org.accdatabase.stockmanager_spring.Service.exception;

public class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
    public ClientException(Exception e) {
        super(e);
    }
}

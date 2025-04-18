package org.accdatabase.stockmanager_spring.Service.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
    public ServerException(Exception e) {
        super(e);
    }

}

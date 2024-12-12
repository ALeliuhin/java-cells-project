package org.example.finalproject.main.exceptions;

public class ServerUnreached extends RuntimeException{
    public ServerUnreached(String message) {
        super(message);
    }
    public ServerUnreached(String message, Throwable cause){
        super(message, cause);
    }
}

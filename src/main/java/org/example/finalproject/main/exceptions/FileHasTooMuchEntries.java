package org.example.finalproject.main.exceptions;

public class FileHasTooMuchEntries extends ExtraExceptions {
    public FileHasTooMuchEntries(String message) {
        super(message);
    }

    public FileHasTooMuchEntries(String message, Throwable cause) {
        super(message, cause);
    }
}

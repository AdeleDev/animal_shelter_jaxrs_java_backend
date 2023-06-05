package org.demo.exceptions;

public class UserNotExistException extends Exception {

    @Override
    public String getMessage() {
        return "No person found by email or password is wrong";
    }
}

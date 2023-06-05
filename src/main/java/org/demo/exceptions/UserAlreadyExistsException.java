package org.demo.exceptions;

public class UserAlreadyExistsException extends Exception {

    private final String email;

    public UserAlreadyExistsException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "Person with email "+ email + " already exists";
    }

}

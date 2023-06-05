package org.demo.exceptions;

public class PetNotExistException extends Exception {

    @Override
    public String getMessage() {
        return "No pets with required parameters exist in repository";
    }
}

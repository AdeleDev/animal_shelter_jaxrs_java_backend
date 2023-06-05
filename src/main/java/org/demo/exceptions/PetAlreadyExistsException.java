package org.demo.exceptions;

public class PetAlreadyExistsException extends Exception {

    private final String name;

    private final String kind;

    public PetAlreadyExistsException(String name, String kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String getMessage() {
        return "Pet " + name + " " + kind + " already exists";
    }

}

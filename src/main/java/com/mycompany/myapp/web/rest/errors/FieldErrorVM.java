package com.mycompany.myapp.web.rest.errors;

import java.io.Serializable;
import java.util.List;

public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String field;

    private final List<String> message;

    public FieldErrorVM(String field, List<String> message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public List<String> getMessage() {
        return message;
    }

}

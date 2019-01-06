package jp.co.run.web.rest.errors;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(index = 1, value = "_field")
    private final String field;

    @JsonProperty(index = 2, value = "_message") 
    private final String message;

    public FieldErrorVM(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}

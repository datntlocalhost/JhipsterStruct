package jp.co.run.web.rest.errors;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jp.co.run.common.constants.ResponseConstants;

public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(index = 1, value = ResponseConstants.SUB_FIELD_ERROR_BODY)
    private final String field;

    @JsonProperty(index = 2, value = ResponseConstants.SUB_MESSAGE_BODY) 
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

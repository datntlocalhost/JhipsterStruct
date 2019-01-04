package com.mycompany.myapp.common.responses;

import java.util.List;

public class ArgumentNotValidResponse extends BaseExceptionResponse {

    private List<Object> fieldErrors;
    
    public ArgumentNotValidResponse(int status, String type, String message) {
        super(status, type, message);
    }

    public ArgumentNotValidResponse(int status, String type, String message, List<Object> fieldErrors) {
        super(status, type, message);
        this.fieldErrors = fieldErrors;
    }

    public List<Object> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<Object> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

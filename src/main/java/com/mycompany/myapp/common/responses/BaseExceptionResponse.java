package com.mycompany.myapp.common.responses;

public class BaseExceptionResponse {

    protected int status;

    protected String type;

    protected String message;

    public BaseExceptionResponse() {}
    
    public BaseExceptionResponse(int status, String type, String message) {
        super();
        this.status = status;
        this.type = type;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

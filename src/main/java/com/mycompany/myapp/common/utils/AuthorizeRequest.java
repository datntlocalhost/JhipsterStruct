package com.mycompany.myapp.common.utils;

import org.springframework.stereotype.Component;

@Component
public class AuthorizeRequest {
    
    private String[] requestURI;

    public AuthorizeRequest() {}

    public AuthorizeRequest(String... antPatterns) {
        super();
        this.requestURI = antPatterns;
    }
    
    public AuthorizeRequest antMatchers(String... antPatterns) {
        this.requestURI = antPatterns;
        return this;
    }

    public String[] getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String[] requestURI) {
        this.requestURI = requestURI;
    }
}

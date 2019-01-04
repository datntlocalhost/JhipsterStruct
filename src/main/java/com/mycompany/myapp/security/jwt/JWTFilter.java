package com.mycompany.myapp.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.common.constants.Constants;
import com.mycompany.myapp.common.constants.HeaderConstants;
import com.mycompany.myapp.common.constants.MsgCodeConstants;
import com.mycompany.myapp.common.responses.BaseExceptionResponse;
import com.mycompany.myapp.config.ApplicationProperties;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
@Component
public class JWTFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    private final MessageSource messageSource;

    private final ApplicationProperties applicationProperties;

    public JWTFilter(TokenProvider tokenProvider, MessageSource messageSource,
        ApplicationProperties applicationProperties) {
        this.tokenProvider = tokenProvider;
        this.messageSource = messageSource;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String jwt = resolveToken(httpServletRequest);
        if (applicationProperties.getRequest().getPermit().contains(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (applicationProperties.getRequest().getDeny().contains(httpServletRequest.getRequestURI())) {
            createResponseFilter(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED,
                accessor().getMessage(MsgCodeConstants.UNAUTHENTICATION_TYPE_CODE),
                accessor().getMessage(MsgCodeConstants.UNAUTHENTICATION_ERROR_CODE));

        } else if (applicationProperties.getRequest().getAuthenticated().contains(httpServletRequest.getRequestURI())) {
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                createResponseFilter(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    accessor().getMessage(MsgCodeConstants.UNAUTHENTICATION_TYPE_CODE),
                    accessor().getMessage(MsgCodeConstants.UNAUTHENTICATION_ERROR_CODE));
            }
        } else {
            createResponseFilter(httpServletResponse, HttpServletResponse.SC_NOT_FOUND,
                accessor().getMessage(MsgCodeConstants.NOT_FOUND_TYPE_CODE),
                accessor().getMessage(MsgCodeConstants.NOT_FOUND_ERROR_CODE));
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HeaderConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HeaderConstants.BEARER_HEADER)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void createResponseFilter(HttpServletResponse response, int status, String type, String message)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BaseExceptionResponse exceptionResponse = new BaseExceptionResponse(status, type, message);
        response.setContentType(HeaderConstants.CONTENT_TYPE_JSON_HEADER);
        response.setCharacterEncoding(Constants.UTF_8_ENCODER);
        response.setStatus(status);
        response.getWriter().write(mapper.writeValueAsString(exceptionResponse));
    }

    private MessageSourceAccessor accessor() {
        
        return new MessageSourceAccessor(messageSource, Constants.APP_LOCALE);
    }
    
    private static boolean antMatchersURI(List<String> uris, String uri) {
        return uris.stream().filter(f -> {
            if (f.equals(uri)) return true;
            return true;
        }).count() > 0;
    }
    
    public static void main(String[] args) {
        List<String> list = new ArrayList<> ();
        list.add("abc");
        list.add("abcd");
        list.add("abce");
        
        System.out.println(antMatchersURI(list, null));
    }
}

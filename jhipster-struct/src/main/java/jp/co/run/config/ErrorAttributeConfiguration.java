package jp.co.run.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.context.request.WebRequest;

import jp.co.run.common.constants.Constants;
import jp.co.run.common.constants.MessageConstants;
import jp.co.run.common.constants.ResponseConstants;

/**
 * The Class ErrorAttributeConfiguration.
 * 
 * @author Korn Nguyen
 */
@Configuration
public class ErrorAttributeConfiguration {

    /** The message source. */
    private final MessageSource messageSource;

    /**
     * Instantiates a new error attribute configuration.
     *
     * @param messageSource the message source
     */
    public ErrorAttributeConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Error attributes.
     *
     * @return the error attributes
     */
    @Bean
    public ErrorAttributes errorAttributes() {

        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                boolean includeStackTrace) {
                Map<String, Object> errorAttributes = new LinkedHashMap<>();
                Integer status = (Integer) webRequest.getAttribute(
                    "javax.servlet.error.status_code",
                    WebRequest.SCOPE_REQUEST);

                errorAttributes.put(ResponseConstants.STATUS_BODY, status);
                errorAttributes.put(ResponseConstants.TYPE_BODY, accessor()
                    .getMessage(MessageConstants.NOT_FOUND_TYPE_CODE));
                errorAttributes.put(ResponseConstants.MESSAGE_BODY,
                    accessor().getMessage(MessageConstants.NOT_FOUND_MSG_CODE));

                return errorAttributes;
            }
        };
    }

    /**
     * Accessor.
     *
     * @return the message source accessor
     */
    private MessageSourceAccessor accessor() {
        return new MessageSourceAccessor(messageSource, Constants.APP_LOCALE);
    }
}

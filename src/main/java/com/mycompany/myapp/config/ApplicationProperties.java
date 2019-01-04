package com.mycompany.myapp.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Spring JWT.
 * <p>
 * Properties are configured in the application.yml file. See {@link io.github.jhipster.config.JHipsterProperties} for a
 * good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /** The request. */
    private final Request request = new Request();

    /**
     * Gets the request.
     *
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * The Class Request.
     */
    public static class Request {

        /** The permit. */
        private List<String> permit = new ArrayList<>();

        /** The deny. */
        private List<String> deny = new ArrayList<>();

        /** The authenticated. */
        private List<String> authenticated = new ArrayList<>();

        /**
         * Gets the permit.
         *
         * @return the permit
         */
        public List<String> getPermit() {
            return permit;
        }

        /**
         * Sets the permit.
         *
         * @param permit the new permit
         */
        public void setPermit(List<String> permit) {
            this.permit = permit;
        }

        /**
         * Gets the deny.
         *
         * @return the deny
         */
        public List<String> getDeny() {
            return deny;
        }

        /**
         * Sets the deny.
         *
         * @param deny the new deny
         */
        public void setDeny(List<String> deny) {
            this.deny = deny;
        }

        /**
         * Gets the authenticated.
         *
         * @return the authenticated
         */
        public List<String> getAuthenticated() {
            return authenticated;
        }

        /**
         * Sets the authenticated.
         *
         * @param authenticated the new authenticated
         */
        public void setAuthenticated(List<String> authenticated) {
            this.authenticated = authenticated;
        }
    }
}

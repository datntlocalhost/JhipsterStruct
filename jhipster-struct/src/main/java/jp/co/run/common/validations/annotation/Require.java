package jp.co.run.common.validations.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import jp.co.run.common.constants.Constants;
import jp.co.run.common.validations.RequireValidator;

@Documented
@Constraint(validatedBy = {RequireValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Require {
    /**
     * Message.
     *
     * @return the string
     */
    String message() default Constants.BLANK;

    /**
     * Error code.
     *
     * @return the string
     */
    String errorCode() default Constants.BLANK;

    /**
     * Item name.
     *
     * @return the string
     */
    String fieldName() default Constants.BLANK;

    /**
     * Groups.
     *
     * @return the class[]
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return the class<? extends payload>[]
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link Require} annotations on the same element.
     *
     * @see javax.validation.constraints.NotNull
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        /**
         * Value.
         *
         * @return the not null[]
         */
        Require[] value();
    }

    
}

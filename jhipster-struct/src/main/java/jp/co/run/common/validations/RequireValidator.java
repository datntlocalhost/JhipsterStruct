package jp.co.run.common.validations;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator;

import jp.co.run.common.validations.annotation.Require;

public class RequireValidator implements HibernateConstraintValidator<Require, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() > 0;
    }

}

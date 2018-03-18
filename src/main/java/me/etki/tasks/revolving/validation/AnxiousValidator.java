package me.etki.tasks.revolving.validation;

import com.google.inject.Inject;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class AnxiousValidator {

    private final Validator validator;

    @Inject
    public AnxiousValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void assertValid(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

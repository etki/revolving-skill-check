package me.etki.tasks.revolving.di.misc;

import com.google.inject.Provider;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorProvider implements Provider<Validator> {
    @Override
    public Validator get() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}

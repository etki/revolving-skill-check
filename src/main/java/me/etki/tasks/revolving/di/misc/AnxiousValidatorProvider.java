package me.etki.tasks.revolving.di.misc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.validation.Validator;

public class AnxiousValidatorProvider implements Provider<AnxiousValidator> {
    private final Validator validator;

    @Inject
    public AnxiousValidatorProvider(Validator validator) {
        this.validator = validator;
    }

    @Override
    public AnxiousValidator get() {
        return new AnxiousValidator(validator);
    }
}

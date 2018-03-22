package me.etki.tasks.revolving.di.misc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.validation.Validator;

public class MiscModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Validator.class).toProvider(ValidatorProvider.class).in(Singleton.class);
        bind(AnxiousValidator.class).toProvider(AnxiousValidatorProvider.class).in(Singleton.class);
    }
}

package me.etki.tasks.revolving.di.misc;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.validation.Validator;

public class MiscModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Validator.class)
                .toProvider(ValidatorProvider.class)
                .in(LazySingletonScope.get());
        bind(AnxiousValidator.class)
                .toProvider(AnxiousValidatorProvider.class)
                .in(LazySingletonScope.get());
    }
}

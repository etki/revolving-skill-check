package me.etki.tasks.revolving.di.misc;

import com.google.inject.AbstractModule;
import me.etki.tasks.revolving.validation.AnxiousValidator;
import org.asynchttpclient.AsyncHttpClient;

import javax.validation.Validator;

public class MiscModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AsyncHttpClient.class).toProvider(AsyncHttpClientProvider.class);
        bind(Validator.class).toProvider(ValidatorProvider.class);
        bind(AnxiousValidator.class).toProvider(AnxiousValidatorProvider.class);
    }
}

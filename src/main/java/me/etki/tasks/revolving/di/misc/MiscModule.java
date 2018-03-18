package me.etki.tasks.revolving.di.misc;

import com.google.inject.AbstractModule;
import org.asynchttpclient.AsyncHttpClient;

public class MiscModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AsyncHttpClient.class).toProvider(AsyncHttpClientProvider.class);
    }
}

package me.etki.tasks.revolving.di.misc;

import com.google.inject.Provider;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

public class AsyncHttpClientProvider implements Provider<AsyncHttpClient> {
    @Override
    public AsyncHttpClient get() {
        return new DefaultAsyncHttpClient();
    }
}

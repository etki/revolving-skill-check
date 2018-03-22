package me.etki.tasks.revolving.api.client;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
public class PayloadRequest<T> extends Request {
    @Getter
    @Setter
    private T payload;
}

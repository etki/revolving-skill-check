package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Accessors(chain = true)
public class Account {
    @Getter
    @Setter
    private UUID id;
    @Getter
    @Setter
    private BigDecimal amount;
}

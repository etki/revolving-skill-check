package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Accessors(chain = true)
public class Transfer {
    @Getter
    @Setter
    private UUID id;
    @Getter
    @Setter
    private UUID source;
    @Getter
    @Setter
    private UUID destination;
    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    @Setter
    private String currency;
}

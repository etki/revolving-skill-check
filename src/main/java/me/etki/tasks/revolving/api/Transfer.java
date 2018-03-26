package me.etki.tasks.revolving.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
public class Transfer {
    @Getter
    @Setter
    private UUID id;
    @Getter
    @Setter
    private UUID source;
    @Getter
    @Setter
    private UUID target;
    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    @Setter
    private String currency;
    @Getter
    @Setter
    private ZonedDateTime executedAt;
}
